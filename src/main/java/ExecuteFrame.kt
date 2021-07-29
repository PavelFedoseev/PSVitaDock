import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import sun.jvm.hotspot.runtime.PerfMemory.initialized
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame


class ExecuteFrame : JFrame(), ActionListener, KeyListener {
    var webcamPanel: WebcamPanel? = null
    var webcam: Webcam? = null
    var button: JButton? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val initialized = AtomicBoolean(false)
    private val dimension = Toolkit.getDefaultToolkit().screenSize
    var isFullScreen = false
    init {
        title = "VitaDock"
        layout = FlowLayout(FlowLayout.CENTER)
        defaultCloseOperation = EXIT_ON_CLOSE

        addKeyListener(this)

        webcam = Webcam.getWebcams()[0]
        if(webcam!=null)
        webcamPanel = WebcamPanel(webcam, false)

        if (webcamPanel != null && webcam != null) {
            webcam!!.viewSize = webcam!!.viewSizes?.get(2)
            webcamPanel!!.preferredSize = webcam!!.viewSize
            webcamPanel!!.isOpaque = true
            webcamPanel!!.background = Color.BLACK

            button = JButton()
            button!!.addActionListener(this)
            button!!.isFocusable = false
            button!!.preferredSize = webcam!!.viewSize

            add(webcamPanel)
            add(button)
            pack()
            isVisible = true

        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (initialized.compareAndSet(false, true)) {
            executor.execute { webcamPanel?.start() }
        }
    }

    override fun keyTyped(e: KeyEvent?) {

    }

    override fun keyPressed(e: KeyEvent?) {
        when(e?.keyCode){
            KeyEvent.VK_ESCAPE -> {
                if(isFullScreen){
                    webcamPanel!!.preferredSize = webcam!!.viewSize
                    isUndecorated = false
                    isFullScreen = false
                    isVisible = false
                }
                else{
                    webcamPanel!!.preferredSize = dimension
                    extendedState = MAXIMIZED_BOTH
                    isUndecorated = true
                    isVisible = true
                    isFullScreen = true
                }
            }
            KeyEvent.VK_ENTER -> {
                if (initialized.compareAndSet(false, true)) {
                    executor.execute { webcamPanel?.start() }
                }
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {

    }
}