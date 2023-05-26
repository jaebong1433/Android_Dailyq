package online.dailyq.api.response

//질문과 답을 받기 위함
data class QuestionAndAnswer(
    val question: Question,
    val answer: Answer
)