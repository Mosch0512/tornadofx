package tornadofx

import javafx.animation.TranslateTransition
import javafx.geometry.Pos
import javafx.scene.layout.Region
import javafx.util.Duration

abstract class SlideIn constructor(private val milliseconds: Double? = 50.0, private val size: Double? = null, private var position: Pos = Pos.TOP_RIGHT) : View() {
    private enum class Vertical {
        TOP,
        BOTTOM
    }
    private enum class Horizontal {
        Left,
        Right
    }

    private lateinit var openSlide: TranslateTransition
    private lateinit var closeSlide: TranslateTransition
    private var firstOpening = true
    override val root = vbox {
        maxHeight = Region.USE_PREF_SIZE
        stackpaneConstraints { alignment = position }
        if (size != null && size >= 11.0) {
            minWidth = size
            prefWidth = size
            maxWidth = size
        } else {
            minWidth = Region.USE_COMPUTED_SIZE
            prefWidth = Region.USE_COMPUTED_SIZE
            maxWidth = Region.USE_PREF_SIZE
        }
        if (milliseconds != null && milliseconds > 0.0) {
            openSlide = TranslateTransition(Duration(milliseconds), this)
            closeSlide = TranslateTransition(Duration(milliseconds), this)
        }
        translateX = boundsInLocal.width
        visibleProperty().set(false)
        managedProperty().bind(visibleProperty())
        disableProperty().bind(!managedProperty())
    }

    private fun slideFromLeft() {
        slideFromLeftOrRight(Horizontal.Left)
    }

    private fun slideFromLeftOrRight(horizontal: Horizontal) {
        var multiplier = 1.0
        if(horizontal == Horizontal.Left) {
            multiplier = -1.0
        }
        if (root.translateX != 0.0 || firstOpening) {
            runAsync {
                openSlide.toX = 0.0
            } ui {
                if (firstOpening && size != null) {
                    root.translateX = multiplier * size
                    firstOpening = false
                } else {
                    root.translateX = multiplier * 350.0
                }
                root.visibleProperty().set(true)
                openSlide.play()
            }
        } else {
            runAsync {
                closeSlide.toX = multiplier * root.boundsInLocal.width
            } ui {
                closeSlide.play()
                closeSlide.setOnFinished {
                    root.visibleProperty().set(false)
                }
            }
        }
    }

    private fun slideFromRight() {
        slideFromLeftOrRight(Horizontal.Right)
    }

    private fun slideFromTop() {
        slideFromTopOrBottom(Vertical.TOP)
    }

    private fun slideFromTopOrBottom(vertical: Vertical) {
        var multiplier = 1.0
        if(vertical == Vertical.TOP) {
            multiplier = -1.0
        }
        if (root.translateY != 0.0 || firstOpening) {
            runAsync {
                openSlide.toY = 0.0
            } ui {
                if (firstOpening && size != null) {
                    root.translateY = multiplier * size
                    firstOpening = false
                } else {
                    root.translateY = multiplier * 350.0
                }
                root.visibleProperty().set(true)
                openSlide.play()

            }
        } else {
            runAsync {
                closeSlide.toY = multiplier * root.boundsInLocal.height
            } ui {
                closeSlide.play()
                closeSlide.setOnFinished {
                    root.visibleProperty().set(false)
                }
            }
        }
    }

    private fun slideFromBottom() {
        slideFromTopOrBottom(Vertical.BOTTOM)
    }

    private fun instantOpenClose() {
        if (root.visibleProperty().get()) {
            root.visibleProperty().set(false)
        } else {
            root.visibleProperty().set(true)
        }
    }

    private fun slideOpenClose() {
        when(position) {
            Pos.TOP_LEFT, Pos.CENTER_LEFT, Pos.BASELINE_LEFT, Pos.BOTTOM_LEFT -> slideFromLeft()
            Pos.TOP_RIGHT, Pos.CENTER_RIGHT, Pos.BASELINE_RIGHT, Pos.BOTTOM_RIGHT -> slideFromRight()
            Pos.TOP_CENTER -> slideFromTop()
            Pos.BOTTOM_CENTER, Pos.CENTER, Pos.BASELINE_CENTER -> slideFromBottom()
        }
    }

    fun slideInOpenClose() {
        if (milliseconds == null || milliseconds == 0.0) {
            instantOpenClose()
        } else {
            slideOpenClose()
        }
    }
}
