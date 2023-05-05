package com.ligastaball

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GameView(val ctx: Context,val attributeSet: AttributeSet): SurfaceView(ctx,attributeSet) {
    var ball = BitmapFactory.decodeResource(ctx.resources,R.drawable.ball)

    var music = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",true)
    var sounds = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("sound",true)
    private var paintB: Paint = Paint(Paint.DITHER_FLAG)
    private var listener: EndListener? = null
    private val random = Random()
    private var millis = 0
    public var isWin = false
    var color1 = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("theme",R.color.light_green)
    public var level = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("level",R.color.light_green)
    var player = MediaPlayer.create(ctx,R.raw.music)
    var sound = MediaPlayer.create(ctx,R.raw.sound)
    var kick = MediaPlayer.create(ctx,R.raw.kick)
    var win = MediaPlayer.create(ctx,R.raw.win)
    var lose = MediaPlayer.create(ctx,R.raw.lose)

    init {
          player.setOnCompletionListener {
            it.start()
        }
        if(music) player.start()
        ball = Bitmap.createScaledBitmap(ball,ball.width/8,ball.height/8,true)
        holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                val canvas = holder.lockCanvas()
                if(canvas!=null) {
                    by = canvas.height/2
                    bx = (canvas.width/2f-ball.width/2f).toInt()
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                paused = true
                player.stop()
            }

        })
        val updateThread = Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!paused) {
                        update.run()
                        millis ++
                    }
                }
            }, 500, 16)
        }

        updateThread.start()
    }

    override fun onDraw(canvas: Canvas?) {
       // anim.onDraw(canvas)
        super.onDraw(canvas)
    }

    var code = -1f
    var c = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_UP -> {
                code = -1f
            }
            MotionEvent.ACTION_DOWN -> {
                code = event.x
                c = 100
                if(dy==0) {
                    dy = 10
                    dx = if(level>1) min(6,level) else 1
                }
                else dy = - dy
                if(sounds) {
                    //sound.pause()
                    sound.seekTo(0)
                    sound.start()
                }
            }
        }
        postInvalidate()
        return true
    }
    var paused = false
    var bx = 0
    var health = 3
    var by = 0
    var dy = 0
    var dx = 0



    val update = Runnable{
        var isEnd = false
        var sc = false
        if(paused) return@Runnable
        try {
            val canvas = holder.lockCanvas()
            canvas.drawColor(ctx.getColor(R.color.green))
            by += dy
            bx += dx
            if(bx>=ball.width+canvas.width || by<=-ball.height || by>=ball.height+canvas.height || bx<=-ball.width) {
                isEnd = true
                if(sounds) {
                    lose.start()
                }
            }
            drawLines(canvas)
            drawBottom(canvas)
            drawTop(canvas)
            if(level>=2) drawBottomAngle(canvas)
            if(level>=1) drawTopAngle(canvas)
            drawVorota(canvas)
            if(c>0) {
                canvas.drawCircle(
                    bx + ball.width / 2f,
                    by + ball.height / 2f,
                    c.toFloat(),
                    Paint().apply {
                        style = Paint.Style.FILL
                        color = ctx.getColor(color1)
                    })
                c-=2
            }
            canvas.drawBitmap(ball,bx.toFloat(),by.toFloat(),paintB)
            holder.unlockCanvasAndPost(canvas)
            if(isWin) isEnd = true
            if(isEnd) {
                Log.d("TAG","END")
                togglePause()
                if(listener!=null) listener!!.end()
            }
            if(sc) {
                listener?.score(health)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    var bottom = Path()
    fun drawBottom(canvas: Canvas) {
        bottom.moveTo(canvas.width/8f,canvas.height/3f*2)
        bottom.lineTo(canvas.width-canvas.width/3f,canvas.height/3f*2)
        bottom.lineTo(canvas.width-canvas.width/3f,canvas.height/3f*2+canvas.height/20)
        bottom.lineTo(canvas.width/8f,canvas.height/3f*2+canvas.height/20)
        bottom.close()
        canvas.drawPath(bottom,Paint().apply {
            style = Paint.Style.FILL
            color = ctx.getColor(color1)
        })
        var rect = RectF()
        bottom.computeBounds(rect,true)
        if(rect.contains(bx+ball.width/2f,by+ball.height.toFloat()) || rect.contains(bx+ball.width/2f,by.toFloat())) {
            dy = - dy
            if(sounds) {
                kick.seekTo(0)
                kick.start()
            }
        }
    }

    var top = Path()
    fun drawTop(canvas: Canvas) {
        top.moveTo(canvas.width/3f,canvas.height/3f)
        top.lineTo(canvas.width-canvas.width/8f,canvas.height/3f)
        top.lineTo(canvas.width-canvas.width/8f,canvas.height/3f+canvas.height/20)
        top.lineTo(canvas.width/3f,canvas.height/3f+canvas.height/20)
        top.close()
        canvas.drawPath(top,Paint().apply {
            style = Paint.Style.FILL
            color = ctx.getColor(color1)
        })
        var rect = RectF()
        top.computeBounds(rect,true)
        if(rect.contains(bx+ball.width/2f,by.toFloat()) || rect.contains(bx+ball.width/2f,by.toFloat()+ball.height)) {
            dy = - dy
            if(sounds) {
                kick.seekTo(0)
                kick.start()
            }
        }
    }

    var bottomAngle = Path()
    fun drawBottomAngle(canvas: Canvas) {
        bottomAngle.moveTo(canvas.width/8f,canvas.height/3f*2)
        bottomAngle.lineTo(canvas.width/8f,canvas.height/3f*2-canvas.height/14f)
        bottomAngle.lineTo(canvas.width/8f+canvas.height/14f,canvas.height/3f*2)
        bottomAngle.close()
        canvas.drawPath(bottomAngle,Paint().apply {
            style = Paint.Style.FILL
            color = ctx.getColor(color1)
        })
        var rect = RectF()
        bottomAngle.computeBounds(rect,true)
        if(rect.contains(bx+ball.width/2f,by.toFloat()+ball.height/2)) {
            dx = abs(dx)
            if(sounds) {
                kick.seekTo(0)
                kick.start()
            }
        }
    }

    var topAngle = Path()
    fun drawTopAngle(canvas: Canvas) {
        topAngle.moveTo(canvas.width-canvas.width/8f,canvas.height/3f+canvas.height/20)
        topAngle.lineTo(canvas.width-canvas.width/8f,canvas.height/3f+canvas.height/14f+canvas.height/20)
        topAngle.lineTo(canvas.width-canvas.width/8f-canvas.height/14f,canvas.height/3f+canvas.height/20)
        topAngle.close()
        canvas.drawPath(topAngle,Paint().apply {
            style = Paint.Style.FILL
            color = ctx.getColor(color1)
        })
        var rect = RectF()
        topAngle.computeBounds(rect,true)
        if(rect.contains(bx+ball.width/2f,by.toFloat()-ball.height/2)) {
            dx = - abs(dx)
            if(sounds) {
                kick.seekTo(0)
                kick.start()
            }
        }
    }

    var vorota = Path()
    var vorota1 = Path()
    fun drawVorota(canvas: Canvas) {
        var x1 = 0f
        var y1 = 0f
        var sym = level<4
        if(sym) {
            x1 = canvas.width/3*2f
            y1 = canvas.height/5*4f
        } else {
            x1 = canvas.width/10f
            y1 = canvas.height/5f
        }
        vorota.moveTo(x1,y1)
        vorota.lineTo(x1+300,y1)
        vorota.lineTo(x1+300,y1+80)
        vorota.lineTo(x1,y1+80)
        vorota.close()

        vorota1.moveTo(x1+40,y1)
        vorota1.lineTo(x1+40,y1-130)
        vorota1.lineTo(x1+260,y1-130)
        vorota1.lineTo(x1+260,y1)
        vorota1.lineTo(x1+220,y1)
        vorota1.lineTo(x1+220,y1-80)
        vorota1.lineTo(x1+80,y1-80)
        vorota1.lineTo(x1+80,y1)
        vorota1.close()
        canvas.drawPath(vorota,Paint().apply {
            style = Paint.Style.FILL
            color = ctx.getColor(color1)
        })
        var rect = RectF()
        vorota.computeBounds(rect,true)
        if(rect.contains(bx+ball.width/2f,(if(!sym) by.toFloat() else by-ball.height).toFloat())) {
            isWin = true
            if(sounds) win.start()
        }
        canvas.drawPath(vorota1,Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        })
        var rect1 = RectF()
        vorota1.computeBounds(rect1,true)
        if(rect1.contains(bx+ball.width/2f,(if(!sym) by.toFloat() else by-ball.height).toFloat())) {
            isWin = true
            if(sounds) win.start()
        }
    }

    fun drawLines(canvas: Canvas) {
        var path = Path()
        var offset = 0f
         for(i in 0..8) {
           if(i%2==0) {
               path.moveTo(50f,canvas.height/8f+offset)
               path.lineTo(canvas.width-50f,canvas.height/8f+offset)
               path.lineTo(canvas.width-50f,canvas.height/8f+canvas.height/14+offset)
               path.lineTo(50f,canvas.height/8f+canvas.height/12f+offset)
               canvas.drawPath(path,Paint().apply {
                   style = Paint.Style.FILL
                   color = ctx.getColor(R.color.gr)
               })
           }
             offset += canvas.height/12f
        }
    }
    fun setEndListener(list: EndListener) {
        this.listener = list
    }
    fun togglePause() {
        paused = !paused
    }
    companion object {
        interface EndListener {
            fun end();
            fun score(score: Int);
        }

    }
}