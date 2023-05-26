package online.dailyq.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.dailyq.db.dao.QuestionDao
import online.dailyq.db.dao.UserDao
import online.dailyq.db.entity.QuestionEntity
import online.dailyq.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        QuestionEntity::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getQuestionDao(): QuestionDao
    
    //싱글톤을 만들 때 자주 사용되는 더블 체크 락킹 패턴을 사용
    //초기화 메서드가 필요 없고 getInstance()메서드가 생성과 가져오는 역할을 모두 함
    //getInstance()메서드를 호출했을 때 이미 생성된 INSTANCE가 있을 경우 생성하지 않고 반환
    //INSTANCE가 비어 있다면 syncronized로 동기화 블록을 만든 후 다시 INSTANCE가 비어있는지 확인하고
    //INSTANCE를 생성해 INSTANCE 변수에 보관함
    companion object {
        const val FILENAME = "dailyq.db"
        var INSTANCE: AppDatabase? = null

        private fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                FILENAME
            ).fallbackToDestructiveMigration()
                .build()
        }

        fun init(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: create(context).also {
                INSTANCE = it
            }
        }

        fun getInstance(context: Context): AppDatabase = INSTANCE ?:
        synchronized(this) {
            INSTANCE ?: create(context).also {
                INSTANCE = it
            }
        }
    }

}