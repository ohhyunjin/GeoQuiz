package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // widgets
    private lateinit var tvQuestion: TextView
    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button
    private lateinit var tvCorrectCount: TextView
    private lateinit var tvIncorrectCount: TextView

    // counters
    private var correctCount = 0
    private var incorrectCount = 0
    var currentIndex: Int = Random.nextInt(0, 5)

    // misc
    private lateinit var questionsList : MutableList<Question>
    private lateinit var questions : Array<Question>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvQuestion = findViewById(R.id.tv_question)
        btnTrue = findViewById(R.id.btn_true)
        btnFalse = findViewById(R.id.btn_false)
        tvCorrectCount = findViewById(R.id.tv_correct_count)
        tvIncorrectCount = findViewById(R.id.tv_incorrect_count)

        currentIndex = Random.nextInt(0, 5)

        val db = FirebaseFirestore.getInstance()

        db.collection("questions")
            .get()
            .addOnCompleteListener { task ->
                questionsList = mutableListOf()

                if (task.isSuccessful) {
                    task.result!!.forEach { document ->
                        questionsList.add(
                            Question(document.data["question"].toString(),
                                document.data["answer"].toString().toBoolean()
                            )
                        )
                    }

                    questions = questionsList.toTypedArray()

                    tvQuestion.text = questions[currentIndex].questionText

                } else {
                    Toast.makeText(this, "Couldn't retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

        tvCorrectCount.text = getString(R.string.correct_count_label, correctCount)
        tvIncorrectCount.text = getString(R.string.incorrect_count_label, incorrectCount)

        btnTrue.setOnClickListener {
            checkAnswer(questions[currentIndex], true)
        }

        btnFalse.setOnClickListener {
            checkAnswer(questions[currentIndex], false)
        }
    }

    private fun checkAnswer(question: Question, answerButton: Boolean) {
        val correctAnswer = question.answer
        if (correctAnswer == answerButton) {
            correctCount++
            tvCorrectCount.text = getString(R.string.correct_count_label, correctCount)
            currentIndex = Random.nextInt(0, 5)
            tvQuestion.text = question.questionText//getString(question.questionTextId)
        } else {
            incorrectCount++
            tvIncorrectCount.text = getString(R.string.incorrect_count_label, incorrectCount)
            currentIndex = Random.nextInt(0, 5)
            tvQuestion.text = question.questionText//getString(question.questionTextId)
        }
    }
}
