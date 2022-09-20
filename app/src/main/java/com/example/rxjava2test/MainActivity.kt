package com.example.rxjava2test

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Flowable.interval
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.reflect.Array.get
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {
    val lessonButton: Button by lazy {findViewById(R.id.btnLessonExs)}
    val subjectExsButton: Button by lazy {findViewById(R.id.btnSubject)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Создание собственного Observable и Flowable
        //суть - показать асинхронность отображения двух TextView
        val user: TextView = findViewById(R.id.tvUser)
        val partner: TextView = findViewById(R.id.tvUser2)

        val users = listOf(
            User("Donna", 25),
            User("Anny", 31),
            User("Stella", 30),
            User("Dima", 33),
            User("Maks", 35),
            User("Den", 22)
        )

        fun usersSours(): Observable<User> {
            return Observable.create { subscriber ->
                for (i in users) {
                    Thread.sleep(2000)
                    subscriber.onNext(i)
                }
            }
        }

        usersSours()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter{it.age >=30}
            .subscribe(
                {
                    user.text = "${it.name} ${it.age}"
                    Log.d("RxJava3", "onNext ${it.name} ${it.age}")
                },
                {
                    Log.d("RxJava3", "error")
                },
                { Log.d("RxJava3", "massage")}
            )


        val patnerSourse= Flowable.just ( User("Donna", 25),
           User("Anny", 31),
           User("Stella", 30),
           User("Dima", 33),
           User("Maks", 35),
           User("Den", 22)
            )

        patnerSourse
            .delay (4, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .onBackpressureBuffer(4)

            .subscribe({
                partner.text = "${it.name} ${it.age}"
                Log.d(
                    "RxJava3", "onNextFlowable ${it.name}"
                )
            }, {}, {}
            )

        lessonButton.setOnClickListener {
            startActivity(Intent(this, LessonExsAcrivity::class.java));
        }
        subjectExsButton.setOnClickListener {
            startActivity(Intent(this, SubjectsActivity::class.java));
        }

    }
}