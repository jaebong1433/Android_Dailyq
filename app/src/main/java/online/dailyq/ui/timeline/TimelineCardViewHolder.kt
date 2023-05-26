package online.dailyq.ui.timeline

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import online.dailyq.R
import online.dailyq.databinding.ItemTimelineCardBinding
import online.dailyq.db.entity.QuestionEntity
import online.dailyq.ui.details.DetailsActivity
import java.time.format.DateTimeFormatter

class TimelineCardViewHolder(val binding: ItemTimelineCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy. M. d.")
    }

    //Question을 매개변수로 받아 레이아웃 파일에서 생성된 ItemTimelineCardBinding을 채움
    //Question.id를 날짜로 변환할 때 DateTimeFormatter를 이용하는데, 매번 생성할 필요가 없기 때문에
    //성능을 위해 모든 TimelineCardViewHolder에서 공유하도록 companion object에 선언
    fun bind(question: QuestionEntity) {
        binding.date.text = DATE_FORMATTER.format(question.id)
        binding.question.text = question.text ?: ""

        binding.answerCount.text = if (question.answerCount > 0) {
            binding.root.context.getString(R.string.answer_count_format, question.answerCount)
        } else {
            binding.root.context.getString(R.string.no_answer_yet)
        }

        //타임라인에서 상세보기 구현
        binding.card.setOnClickListener {
            val context = binding.root.context

            context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                putExtra(DetailsActivity.EXTRA_QID, question.id)
            })
        }
    }
}