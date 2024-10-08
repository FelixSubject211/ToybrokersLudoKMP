package com.toybrokers.ludo.features.game.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.toybrokers.ludo.domain.entities.Dice

@Composable
fun AnimatedDice(
    dice: Dice,
    onTap: (Dice) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRotating by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isRotating) 360f else 0f,
        animationSpec = tween(durationMillis = 100),
        finishedListener = {
            isRotating = false
        }
    )

    LaunchedEffect(dice.uuidForUIUpdates) {
        isRotating = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(64.dp)
            .rotate(rotation)
            .background(Color.Red, RoundedCornerShape(8.dp))
            .clickable {
                if (!isRotating) {
                    onTap(dice.rolled())
                }
            }
    ) {
        DrawDiceFace(dice.diceNumber)
    }
}



@Composable
private fun DrawDiceFace(number: Int) {
    Canvas(modifier = Modifier.size(64.dp)) {
        when (number) {
            1 -> drawCircleAtCenter()
            2 -> {
                drawCircleTopLeft()
                drawCircleBottomRight()
            }
            3 -> {
                drawCircleTopLeft()
                drawCircleAtCenter()
                drawCircleBottomRight()
            }
            4 -> {
                drawCircleTopLeft()
                drawCircleTopRight()
                drawCircleBottomLeft()
                drawCircleBottomRight()
            }
            5 -> {
                drawCircleTopLeft()
                drawCircleTopRight()
                drawCircleAtCenter()
                drawCircleBottomLeft()
                drawCircleBottomRight()
            }
            6 -> {
                drawCircleTopLeft()
                drawCircleTopRight()
                drawCircleMiddleLeft()
                drawCircleMiddleRight()
                drawCircleBottomLeft()
                drawCircleBottomRight()
            }
        }
    }
}

private fun DrawScope.drawCircleAtCenter() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = center
    )
}

private fun DrawScope.drawCircleTopLeft() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = Offset(x = size.width * 0.25f, y = size.height * 0.25f)
    )
}

private fun DrawScope.drawCircleTopRight() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = Offset(x = size.width * 0.75f, y = size.height * 0.25f)
    )
}

private fun DrawScope.drawCircleBottomLeft() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = Offset(x = size.width * 0.25f, y = size.height * 0.75f)
    )
}

private fun DrawScope.drawCircleBottomRight() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = Offset(x = size.width * 0.75f, y = size.height * 0.75f)
    )
}

private fun DrawScope.drawCircleMiddleLeft() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = Offset(x = size.width * 0.25f, y = size.height * 0.5f)
    )
}

private fun DrawScope.drawCircleMiddleRight() {
    drawCircle(
        color = Color.White,
        radius = 6.dp.toPx(),
        center = Offset(x = size.width * 0.75f, y = size.height * 0.5f)
    )
}
