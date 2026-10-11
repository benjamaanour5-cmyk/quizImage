package com.example.quizimages
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class Question(
    val texte: String,
    val image: Int,
    val choix: Array<String>,
    val bonneReponse: Int,
    val descriptionImage: String
)

class MainActivity : AppCompatActivity() {

    private val questions = arrayOf(
        Question(
            "Combien de côtés possède cette forme ?",
            R.drawable.triangle,
            arrayOf("3 côtés", "4 côtés", "5 côtés"),
            0,
            "Triangle bleu"
        ),
        Question(
            "Quelle forme possède quatre côtés égaux et quatre angles droits ?",
            R.drawable.carre,
            arrayOf("Rectangle", "Carré", "Triangle"),
            1,
            "Carré vert"
        ),
        Question(
            "Quelle forme voyez-vous ?",
            R.drawable.cercle,
            arrayOf("Étoile", "Cercle", "Carré"),
            1,
            "Cercle orange"
        ),
        Question(
            "Combien de pointes possède cette forme ?",
            R.drawable.etoile,
            arrayOf("4 pointes", "6 pointes", "5 pointes"),
            2,
            "Étoile jaune à cinq pointes"
        ),
        Question(
            "Quelle forme possède quatre angles droits et deux côtés longs ?",
            R.drawable.rectangle,
            arrayOf("Rectangle", "Cercle", "Triangle"),
            0,
            "Rectangle violet"
        )
    )

    private var indiceQuestion = 0
    private var score = 0
    private var aRepondu = false

    private lateinit var txtScore: TextView
    private lateinit var txtProgression: TextView
    private lateinit var txtNumero: TextView
    private lateinit var imgQuestion: ImageView
    private lateinit var txtQuestion: TextView
    private lateinit var txtFeedback: TextView
    private lateinit var progressQuiz: ProgressBar
    private lateinit var btnChoix: List<Button>
    private lateinit var btnSuivant: Button
    private lateinit var layoutResultat: View
    private lateinit var txtResultat: TextView
    private lateinit var btnRejouer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtScore = findViewById(R.id.txt_score)
        txtProgression = findViewById(R.id.txt_progression)
        txtNumero = findViewById(R.id.txt_numero)
        imgQuestion = findViewById(R.id.img_question)
        txtQuestion = findViewById(R.id.txt_question)
        txtFeedback = findViewById(R.id.txt_feedback)
        progressQuiz = findViewById(R.id.progress_quiz)

        btnChoix = listOf(
            findViewById(R.id.btn_choix1),
            findViewById(R.id.btn_choix2),
            findViewById(R.id.btn_choix3)
        )

        btnSuivant = findViewById(R.id.btn_suivant)
        layoutResultat = findViewById(R.id.layout_resultat)
        txtResultat = findViewById(R.id.txt_resultat)
        btnRejouer = findViewById(R.id.btn_rejouer)

        btnChoix.forEachIndexed { index, bouton ->
            bouton.setOnClickListener {
                verifierReponse(index)
            }
        }

        btnSuivant.setOnClickListener {
            if (indiceQuestion == questions.lastIndex) {
                afficherResultat()
            } else {
                indiceQuestion++
                afficherQuestion()
            }
        }

        btnRejouer.setOnClickListener {
            recommencerQuiz()
        }

        afficherQuestion()
    }

    private fun afficherQuestion() {
        val question = questions[indiceQuestion]
        aRepondu = false

        txtNumero.text =
            "Question ${indiceQuestion + 1} sur ${questions.size}"

        txtQuestion.text = question.texte
        imgQuestion.setImageResource(question.image)
        imgQuestion.contentDescription = question.descriptionImage

        btnChoix.forEachIndexed { index, bouton ->
            bouton.text = question.choix[index]
            bouton.isEnabled = true
        }

        txtFeedback.visibility = View.GONE
        btnSuivant.isEnabled = false

        btnSuivant.text =
            if (indiceQuestion == questions.lastIndex) {
                "Voir le résultat"
            } else {
                "Question suivante"
            }

        mettreAJourBandeau()
    }

    private fun verifierReponse(indiceChoix: Int) {
        if (aRepondu) return

        aRepondu = true
        val question = questions[indiceQuestion]
        val estCorrect = indiceChoix == question.bonneReponse

        if (estCorrect) {
            score++
            txtFeedback.text = "Bonne réponse ! Bravo."
            txtFeedback.setTextColor(Color.rgb(46, 125, 50))
        } else {
            txtFeedback.text =
                "Mauvaise réponse. Bonne réponse : " +
                        question.choix[question.bonneReponse]
            txtFeedback.setTextColor(Color.rgb(198, 40, 40))
        }

        txtFeedback.visibility = View.VISIBLE

        btnChoix.forEach { it.isEnabled = false }

        btnSuivant.isEnabled = true
        mettreAJourBandeau()
    }

    private fun mettreAJourBandeau() {
        val reponsesDonnees =
            indiceQuestion + if (aRepondu) 1 else 0

        txtScore.text = "Score : $score/${questions.size}"
        progressQuiz.progress = reponsesDonnees
        txtProgression.text =
            "$reponsesDonnees réponse(s) sur ${questions.size}"
    }

    private fun afficherResultat() {
        txtResultat.text =
            "Quiz terminé !\nVotre score : $score/${questions.size}"

        layoutResultat.visibility = View.VISIBLE

        imgQuestion.visibility = View.GONE
        txtNumero.visibility = View.GONE
        txtQuestion.visibility = View.GONE
        txtFeedback.visibility = View.GONE
        btnSuivant.visibility = View.GONE
        btnChoix.forEach { it.visibility = View.GONE }
    }

    private fun recommencerQuiz() {
        indiceQuestion = 0
        score = 0
        aRepondu = false

        layoutResultat.visibility = View.GONE
        imgQuestion.visibility = View.VISIBLE
        txtNumero.visibility = View.VISIBLE
        txtQuestion.visibility = View.VISIBLE
        btnSuivant.visibility = View.VISIBLE
        btnChoix.forEach { it.visibility = View.VISIBLE }

        afficherQuestion()
    }
}
