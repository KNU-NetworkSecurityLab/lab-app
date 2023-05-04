package lab.nsl.nsl_app.pages.easteregg

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lab.nsl.nsl_app.databinding.ActivityMazeRunnerBinding

class MazeRunnerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMazeRunnerBinding.inflate(layoutInflater) }
    private lateinit var maze: Array<Array<View>>
    private lateinit var visited: Array<Array<Int>>
    private var mazeSize = 50
    private var mazeRowCnt = 0
    private var mazeColCnt = 0
    private val dx = arrayOf(0, 0, 1, -1)
    private val dy = arrayOf(1, -1, 0, 0)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // hide status bar
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        // hide navigation bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION



        makeMaze()
    }

    private fun makeMaze() {
        binding.run {
            val deviceHeight = resources.displayMetrics.heightPixels
            val deviceWidth = resources.displayMetrics.widthPixels

            maze = Array(mazeRowCnt) { Array(mazeColCnt) { View(this@MazeRunnerActivity) } }
            visited = Array(mazeRowCnt) { Array(mazeColCnt) { 0 } }


            // 미로 생성
            mazeRunnerGrid.apply {
                rowCount = mazeRowCnt
                columnCount = mazeColCnt
            }
            

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)

                maze[0][0].setBackgroundColor(0xFFFFFFFF.toInt())
                makeMap(0, 0)
            }
        }
    }

    private suspend fun makeMap(x: Int, y: Int) {
        visited[x][y] = 1

        val direction = (0..3).shuffled()

        outer@ for (i in direction) {
            val nx = x + dx[i]
            val ny = y + dy[i]

            if (nx !in 0 until mazeRowCnt || ny !in 0 until mazeColCnt || visited[nx][ny] == 1) continue

            // 이미 방문한 곳과 이어진다면
            for (j in 0 until 4) {
                // 지나온 곳의 반대방향
                if (i == 0 && j == 1) continue
                if (i == 1 && j == 0) continue
                if (i == 2 && j == 3) continue
                if (i == 3 && j == 2) continue

                val nnx = nx + dx[j]
                val nny = ny + dy[j]

                if (nnx in 0 until mazeRowCnt && nny in 0 until mazeColCnt && visited[nnx][nny] == 1) continue@outer
            }

            delay(30)

            maze[nx][ny].setBackgroundColor(0xFFFFFFFF.toInt())
            makeMap(nx, ny)
        }
    }
}