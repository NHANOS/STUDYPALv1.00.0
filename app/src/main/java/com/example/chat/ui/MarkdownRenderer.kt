package com.example.chat.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private sealed class MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock()
    data class CodeBlock(val language: String, val code: String) : MarkdownBlock()
    data class ListItem(val isNumbered: Boolean, val prefix: String, val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
}

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    baseColor: Color = Color.White
) {
    val blocks = remember(text) { parseMarkdownBlocks(text) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for ((index, block) in blocks.withIndex()) {
            when (block) {
                is MarkdownBlock.Heading -> {
                    val fontSize = when (block.level) {
                        1 -> 20.sp
                        2 -> 18.sp
                        else -> 16.sp
                    }
                    Text(
                        text = parseInlineMarkdown(block.text, baseColor),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        color = baseColor,
                        lineHeight = (fontSize.value * 1.3).sp,
                        modifier = Modifier.padding(top = if (index > 0) 4.dp else 0.dp)
                    )
                }

                is MarkdownBlock.CodeBlock -> {
                    CodeBlockView(
                        language = block.language,
                        code = block.code
                    )
                }

                is MarkdownBlock.ListItem -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = block.prefix,
                            color = Color(0xFF64D2FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            modifier = Modifier.width(22.dp)
                        )
                        Text(
                            text = parseInlineMarkdown(block.text, baseColor),
                            color = baseColor,
                            fontSize = 14.5.sp,
                            lineHeight = 21.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                is MarkdownBlock.Paragraph -> {
                    Text(
                        text = parseInlineMarkdown(block.text, baseColor),
                        color = baseColor,
                        fontSize = 14.5.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CodeBlockView(
    language: String,
    code: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCopied by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val displayLang = if (language.isNotBlank()) language.uppercase() else "CODE"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF141724))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
    ) {
        // Header with Language and Copy Code Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E2235))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayLang,
                color = Color.White.copy(alpha = 0.70f),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.10f))
                    .border(0.8.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Copied Code", code)
                        clipboard.setPrimaryClip(clip)
                        isCopied = true
                        Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                        scope.launch {
                            delay(2000)
                            isCopied = false
                        }
                    },
                    modifier = Modifier
                        .size(22.dp)
                        .testTag("copy_code_btn")
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                        contentDescription = "Copy Code",
                        tint = if (isCopied) Color(0xFF06D6A0) else Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isCopied) "Copied!" else "Copy Code",
                    color = if (isCopied) Color(0xFF06D6A0) else Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Code Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            Text(
                text = code,
                color = Color(0xFFE2E8F0),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 19.sp
            )
        }
    }
}

private fun parseMarkdownBlocks(rawText: String): List<MarkdownBlock> {
    val blocks = mutableListOf<MarkdownBlock>()
    val lines = rawText.lines()
    var index = 0

    while (index < lines.size) {
        val line = lines[index]

        // Check for Code Block Start
        if (line.trimStart().startsWith("```")) {
            val lang = line.trimStart().removePrefix("```").trim()
            val codeLines = mutableListOf<String>()
            index++
            while (index < lines.size && !lines[index].trimStart().startsWith("```")) {
                codeLines.add(lines[index])
                index++
            }
            if (index < lines.size) index++ // Skip closing ```
            blocks.add(MarkdownBlock.CodeBlock(lang, codeLines.joinToString("\n")))
            continue
        }

        // Check for Headings
        val trimmed = line.trim()
        if (trimmed.startsWith("### ")) {
            blocks.add(MarkdownBlock.Heading(3, trimmed.removePrefix("### ").trim()))
            index++
            continue
        } else if (trimmed.startsWith("## ")) {
            blocks.add(MarkdownBlock.Heading(2, trimmed.removePrefix("## ").trim()))
            index++
            continue
        } else if (trimmed.startsWith("# ")) {
            blocks.add(MarkdownBlock.Heading(1, trimmed.removePrefix("# ").trim()))
            index++
            continue
        }

        // Check for Bullet Lists (- or * or •)
        if (trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("• ")) {
            val prefix = "• "
            val itemText = trimmed.substring(2).trim()
            blocks.add(MarkdownBlock.ListItem(false, prefix, itemText))
            index++
            continue
        }

        // Check for Numbered Lists (e.g. "1. ")
        val numberRegex = Regex("""^(\d+)\.\s+(.*)""")
        val numberMatch = numberRegex.find(trimmed)
        if (numberMatch != null) {
            val num = numberMatch.groupValues[1]
            val itemText = numberMatch.groupValues[2]
            blocks.add(MarkdownBlock.ListItem(true, "$num.", itemText))
            index++
            continue
        }

        // Regular paragraph or consecutive non-empty lines
        if (trimmed.isNotEmpty()) {
            val paragraphLines = mutableListOf<String>()
            while (index < lines.size) {
                val curr = lines[index]
                val currTrimmed = curr.trim()
                if (currTrimmed.isEmpty() ||
                    currTrimmed.startsWith("```") ||
                    currTrimmed.startsWith("#") ||
                    currTrimmed.startsWith("- ") ||
                    currTrimmed.startsWith("* ") ||
                    currTrimmed.startsWith("• ") ||
                    numberRegex.containsMatchIn(currTrimmed)
                ) {
                    break
                }
                paragraphLines.add(curr)
                index++
            }
            blocks.add(MarkdownBlock.Paragraph(paragraphLines.joinToString("\n")))
        } else {
            index++
        }
    }

    return blocks
}

/**
 * Parses bold (**text**), italic (*text*), and inline code (`code`) into an AnnotatedString
 */
private fun parseInlineMarkdown(text: String, baseColor: Color): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        while (cursor < text.length) {
            // Check for Inline Code: `code`
            if (text[cursor] == '`') {
                val end = text.indexOf('`', cursor + 1)
                if (end != -1) {
                    val codeContent = text.substring(cursor + 1, end)
                    pushStyle(
                        SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            background = Color(0x40000000),
                            color = Color(0xFFFFD166),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    append(" $codeContent ")
                    pop()
                    cursor = end + 1
                    continue
                }
            }

            // Check for Bold: **text**
            if (cursor + 1 < text.length && text[cursor] == '*' && text[cursor + 1] == '*') {
                val end = text.indexOf("**", cursor + 2)
                if (end != -1) {
                    val boldContent = text.substring(cursor + 2, end)
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = baseColor))
                    append(boldContent)
                    pop()
                    cursor = end + 2
                    continue
                }
            }

            // Check for Italic: *text*
            if (text[cursor] == '*') {
                val end = text.indexOf('*', cursor + 1)
                if (end != -1) {
                    val italicContent = text.substring(cursor + 1, end)
                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic, color = baseColor))
                    append(italicContent)
                    pop()
                    cursor = end + 1
                    continue
                }
            }

            append(text[cursor])
            cursor++
        }
    }
}
