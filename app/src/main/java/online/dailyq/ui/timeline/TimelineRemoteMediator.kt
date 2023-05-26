package online.dailyq.ui.timeline

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import online.dailyq.api.ApiService
import online.dailyq.db.AppDatabase
import online.dailyq.db.entity.QuestionEntity
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.LocalDate

@OptIn(ExperimentalPagingApi::class)
class TimelineRemoteMediator(val api: ApiService, val db: AppDatabase) :
    RemoteMediator<Int, QuestionEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    //load()메서드는 loadType에 따라 요청할 질문 목록의 시작 날짜를 계산하고 서버에서 받은 데이터를
    //Room에 저장한 후 Pager에 결과를 반환함
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QuestionEntity>
    ): MediatorResult {
        val pageSize = state.config.pageSize
        val today = LocalDate.now()

        val fromDate = when (loadType) {
            LoadType.REFRESH -> { //모든 데이터를 삭제하고 오늘 날짜에 데이터부터 받아옴
                today
            }
            LoadType.PREPEND -> {//목록의 앞부분 데이터를 불러와야 함
                val firstItem = state.firstItemOrNull()//이전에 불러온 데이터의 가장 앞부분 가져올 수 있음

                //처음 목록을 표시해 불러온 데이터가 없어 firstItem이 null인 경우이거나 이미 모두 DB에 있어
                //firstItem의 id가 서버에서 데이터를 불러올 필요가 없는 경우에는
                //MediatorResult.Success(endOfPaginationReached = false or true)반환함
                if (firstItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }

                if (firstItem.id >= today) {
                    //endOfPaginationReached = true면 Pager는 해당 LoadType에 대해서 더 이상
                    //load()메서드를 호출하지 않음
                    return MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    firstItem.id.plusDays(pageSize.toLong())
                    val prevKey = firstItem.id
                    if (prevKey > today) {
                        today
                    } else {
                        prevKey
                    }
                }
            }

            //목록의 다음 데이터를 불러오는데 마지막 데이터에서 하루 전의 날짜를 fromDate로 사용 
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    today
                } else {
                    lastItem.id.minusDays(1)
                }
            }
        }

        try {
            val questions = api.getQuestions(fromDate, pageSize).body()
            val endOfPaginationReached = questions.isNullOrEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getQuestionDao().deleteAll()
                }

                questions?.map {
                    QuestionEntity(it.id, it.text, it.answerCount, it.updatedAt, it.createdAt)
                }?.let {
                    db.getQuestionDao().insertOrReplace(it)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: SocketTimeoutException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}
