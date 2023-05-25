package online.dailyq.ui.timeline

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import online.dailyq.api.response.Question
import online.dailyq.databinding.ItemTimelineCardBinding

//PageDataAdapter는 Pager가 불러온 데이터를 백그라운드에서 가공해 불러옴
//로딩 상태를 addLoadStateListener()메서드나 withLoadStateHeader(),
//withLoadStateFooter(), withLoadStateHeaderAndFooter()메서드들로 수신할 수 있음
//로딩에 실패했을 때 retry()메서드로 다시 요청하거나 refresh()메서드로 데이터를 새로고침할 수 있음
class TimelineAdapter(val context: Context) :
    PagingDataAdapter<Question, TimelineCardViewHolder>(QuestionComparator){

        object QuestionComparator: DiffUtil.ItemCallback<Question>(){
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem == newItem
            }
        }

    val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineCardViewHolder {
        return TimelineCardViewHolder(ItemTimelineCardBinding.
        inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TimelineCardViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}