@file:Suppress("unused")
package tornadofx

import javafx.animation.TranslateTransition
import javafx.event.EventTarget
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import javafx.util.Duration
import tornadofx.*

abstract class SlideIn : View {

    var milliseconds: Double = 100.00
    var size: Point2D = Point2D(0.0, 0.0)
    var position: Pos = Pos.TOP_RIGHT
    var direction: Direction = Direction.RIGHT_TO_LEFT
    private lateinit var openSlide: TranslateTransition
    private lateinit var closeSlide: TranslateTransition
    private var firstOpening = true

    override val root = vbox ()

    constructor() {
        createSlide()
    }

    constructor(milliseconds: Double) {
        this.milliseconds = milliseconds
        createSlide()
    }

    constructor(size: Point2D) {
        this.size = size
        createSlide()
    }

    constructor(position: Pos) {
        this.position = position
        createSlide()
    }

    constructor(direction: Direction) {
        this.direction = direction
        createSlide()
    }

    constructor(milliseconds: Double, size: Point2D) {
        this.milliseconds = milliseconds
        this.size = size
        createSlide()
    }

    constructor(milliseconds: Double, position: Pos) {
        this.milliseconds = milliseconds
        this.position = position
        createSlide()
    }

    constructor(milliseconds: Double, direction: Direction) {
        this.milliseconds = milliseconds
        this.direction = direction
        createSlide()
    }

    constructor(size: Point2D, position: Pos) {
        this.size = size
        this.position = position
        createSlide()
    }

    constructor(size: Point2D, direction: Direction) {
        this.size = size
        this.direction = direction
        createSlide()
    }

    constructor(position: Pos, direction: Direction) {
        this.position = position
        this.direction = direction
        createSlide()
    }

    constructor(milliseconds: Double, size: Point2D, position: Pos) {
        this.milliseconds = milliseconds
        this.size = size
        this.position = position
        createSlide()
    }

    constructor(milliseconds: Double, size: Point2D, direction: Direction) {
        this.milliseconds = milliseconds
        this.size = size
        this.direction = direction
        createSlide()
    }

    constructor(milliseconds: Double, position: Pos, direction: Direction) {
        this.milliseconds = milliseconds
        this.position = position
        this.direction = direction
        createSlide()
    }

    constructor(size: Point2D, position: Pos, direction: Direction) {
        this.size = size
        this.position = position
        this.direction = direction
        createSlide()
    }

    constructor(milliseconds: Double, size: Point2D, position: Pos, direction: Direction) {
        this.milliseconds = milliseconds
        this.size = size
        this.position = position
        this.direction = direction
        createSlide()
    }

    enum class Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP
    }

    private fun slideFromLeftOrRight() {
        var multiplier = 1.0
        if (direction == Direction.LEFT_TO_RIGHT) {
            multiplier = -1.0
        }
        if (root.translateX != 0.0 || firstOpening) {
            runAsync {
                openSlide.toX = 0.0

            } ui {
                if (firstOpening && size!!.x > 0.0) {
                    root.translateX = multiplier * size!!.x
                } else {
                    root.translateX = multiplier * 350.0
                }
                root.visibleProperty().set(true)
                openSlide.play()
                firstOpening = false
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

    private fun slideFromTopOrBottom() {
        var multiplier = 1.0
        if (direction == Direction.TOP_TO_BOTTOM) {
            multiplier = -1.0
        }
        if (root.translateY != 0.0 || firstOpening) {
            runAsync {
                openSlide.toY = 0.0

            } ui {
                if (firstOpening && size!!.y > 0.0) {
                    root.translateY = multiplier * size!!.y
                } else {
                    root.translateY = multiplier * 350.0
                }
                root.visibleProperty().set(true)
                openSlide.play()
                firstOpening = false
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

    private fun instantOpenClose() {
        if (root.visibleProperty().get()) {
            root.visibleProperty().set(false)
        } else {
            root.visibleProperty().set(true)
        }
    }

    private fun slideOpenClose() {
        when (direction) {
            Direction.LEFT_TO_RIGHT, Direction.RIGHT_TO_LEFT -> slideFromLeftOrRight()
            Direction.TOP_TO_BOTTOM, Direction.BOTTOM_TO_TOP -> slideFromTopOrBottom()
        }
    }

    fun slideInOpenClose() {
        if (milliseconds == null || milliseconds == 0.0) {
            instantOpenClose()
        } else {
            slideOpenClose()
        }
    }

    init {
        this.primaryStage.sceneProperty().onChange {
            closeAtClickSomewhereElse()
        }
        closeAtClickSomewhereElse()
    }

    private fun createSlide() {
        with(root) {
            maxHeight = Region.USE_PREF_SIZE
            stackpaneConstraints { alignment = position }
            println(position.toString())
            if (size != null && size.x >= 11.0) {
                minWidth = size.x
                prefWidth = size.x
                maxWidth = size.x
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
    }

    private fun closeAtClickSomewhereElse() {
        if (this.primaryStage.sceneProperty().get() != null) {
            this.primaryStage.sceneProperty().get().addEventFilter(MouseEvent.MOUSE_CLICKED) {
                if (root.visibleProperty().get() && !isClicked(root, it.target)) {
                    slideOpenClose()
                    it.consume()
                }
            }
        }
    }

    private fun isClicked(node: Node, target: EventTarget): Boolean {
        return if (target == node) {
            true
        } else {
            isChildClicked(node, target)
        }
    }

    private fun isChildClicked(node: Node, target: EventTarget): Boolean {
        if (node.getChildList() != null && node.getChildList()!!.isNotEmpty()) {
            if (node.getChildList()!!.contains(target)) {
                return true
            } else {
                for (it in node.getChildList()!!) {
                    if (isClicked(it, target)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
