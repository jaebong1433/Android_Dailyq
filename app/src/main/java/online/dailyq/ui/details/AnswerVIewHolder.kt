package online.dailyq.ui.details

import android.content.Intent
import android.text.format.DateUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import online.dailyq.R
import online.dailyq.api.response.Answer
import online.dailyq.databinding.ItemAnswerBinding
import online.dailyq.ui.image.ImageViewerActivity
import online.dailyq.ui.profile.ProfileActivity

//사용자가 답을 쓴 시간은 경과 시간으로 표시하는데,
//DateUtils에 준비되어 있는 getRelativeTimeSpanString()메서드를 사용
//getRelativeTimeSpanString()메서드는 매개변수로 전달되는 시간에서부터 현재 시간까지 얼마나 경과했는지 경과시간에
//따라 'n분 전', '어제', '그저께', 'n일 전', 'yyyy년 MM월 dd일'등의 표현으로 반환함
class AnswerViewHolder(val binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(answer: Answer) {
        binding.userName.text = answer.answerer?.name

        if (!answer.answerer?.photo.isNullOrBlank()) {
            binding.userPhoto.load(answer.answerer?.photo) {
                placeholder(R.drawable.ph_user)
                error(R.drawable.ph_user)
                transformations(CircleCropTransformation())
            }
        }

        binding.userPhoto.setOnClickListener {
            val context = itemView.context
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(ProfileActivity.EXTRA_UID, answer.answerer?.id)
            })
        }

        binding.textAnswer.text = answer.text
        binding.textAnswer.isVisible = !answer.text.isNullOrEmpty()
        binding.photoAnswer.load(answer.photo) {
            placeholder(R.drawable.ph_image)
            error(R.drawable.ph_image)
        }
        binding.photoAnswer.isVisible = !answer.photo.isNullOrEmpty()
        binding.photoAnswer.setOnClickListener {
            val context = itemView.context
            context.startActivity(Intent(context, ImageViewerActivity::class.java).apply {
                putExtra(ImageViewerActivity.EXTRA_URL, answer.photo)
            })
        }

        binding.elapsedTime.text = DateUtils.getRelativeTimeSpanString(answer.createdAt.time)
    }
}