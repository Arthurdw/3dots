package com.arthurdw.threedots.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import coil.imageLoader
import coil.request.ImageRequest
import com.arthurdw.threedots.R
import com.arthurdw.threedots.data.DefaultAppContainer
import com.arthurdw.threedots.data.objects.NewsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
    private suspend fun sendNotification(news: NewsItem) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "news",
                "Stock News",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        Log.d("NewsWorker", "Sending notification for ${news.title}")
        Log.d("NewsWorker", "Image URL: ${news.imageUrl}")
        val request = ImageRequest.Builder(context)
            .data(news.imageUrl)
            .build()

        val imageResult = request.context.imageLoader.execute(request)
        val image = (imageResult.drawable as BitmapDrawable).bitmap

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            news.getShareIntent(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "news")
            .setContentTitle(news.title)
            .setContentText(news.snippet)
            .setLargeIcon(image)
            .setSmallIcon(R.drawable.ic_dots)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }

    private suspend fun performNewsAction() {
        val container = DefaultAppContainer(context)
        val news = container.networkRepository.getNews()
        val first = news.firstOrNull()

        if (first != null) sendNotification(first)
        else throw Exception("No news found")
    }

    override fun doWork(): Result {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                performNewsAction()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}