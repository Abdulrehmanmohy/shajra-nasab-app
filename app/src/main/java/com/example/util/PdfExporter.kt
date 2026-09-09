package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.FamilyMember
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    fun generateAndOpenFamilyTreePdf(
        context: Context,
        familyMembers: List<FamilyMember>,
        isPremium: Boolean = true
    ) {
        if (familyMembers.isEmpty()) {
            Toast.makeText(context, "Shajra me filhal koi fard nahi hai!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfDocument = PdfDocument()
            val pageWidth = 595 // A4 standard width in points (72 dpi)
            val pageHeight = 842 // A4 standard height in points
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            val titlePaint = Paint().apply {
                color = Color.rgb(20, 40, 80)
                textSize = 22f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            val subtitlePaint = Paint().apply {
                color = Color.rgb(80, 80, 80)
                textSize = 12f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                isAntiAlias = true
            }

            val headerPaint = Paint().apply {
                color = Color.rgb(30, 90, 160)
                textSize = 14f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            val bodyPaint = Paint().apply {
                color = Color.rgb(40, 40, 40)
                textSize = 11f
                typeface = Typeface.DEFAULT
                isAntiAlias = true
            }

            val watermarkPaint = Paint().apply {
                color = Color.rgb(220, 220, 220)
                textSize = 40f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            // Draw Background Header Box
            val bgPaint = Paint().apply {
                color = Color.rgb(240, 245, 255)
            }
            canvas.drawRect(20f, 20f, (pageWidth - 20).toFloat(), 90f, bgPaint)

            // Draw Title & Subtitle
            canvas.drawText("KHANDANI SHAJRA-E-NASAB (شجرہ نسب)", 40f, 52f, titlePaint)
            val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
            canvas.drawText("Official Family Lineage Document • Generated on $currentDate", 40f, 75f, subtitlePaint)

            var y = 120f

            // Group Members by Generation
            val grouped = familyMembers.groupBy { it.generation }.toSortedMap()

            grouped.forEach { (gen, members) ->
                val genName = when (gen) {
                    1 -> "Pehli Nasal (Aala Ancestors / Great Grandparents)"
                    2 -> "Doosri Nasal (Grandparents / Dada Dadi)"
                    3 -> "Teesri Nasal (Parents / Abba Ammi)"
                    4 -> "Chauthi Nasal (Current Generation / Aap / Sibling)"
                    5 -> "Panchwin Nasal (Children / Aulad)"
                    else -> "Nasal $gen"
                }

                if (y > pageHeight - 60) {
                    // Start new page if overflowing
                    pdfDocument.finishPage(page)
                    val newPage = pdfDocument.startPage(pageInfo)
                    y = 40f
                }

                // Gen Header Banner
                val genBgPaint = Paint().apply { color = Color.rgb(230, 238, 250) }
                canvas.drawRect(30f, y - 14f, (pageWidth - 30).toFloat(), y + 6f, genBgPaint)
                canvas.drawText("📌 $genName", 35f, y, headerPaint)
                y += 24f

                members.forEach { m ->
                    val line = "• ${m.name}" +
                            (if (m.fatherName.isNotBlank()) " (s/o ${m.fatherName})" else "") +
                            " — [${m.relation}]" +
                            (if (m.casteTribe.isNotBlank()) " | Zaat: ${m.casteTribe}" else "") +
                            (if (m.nativeCity.isNotBlank()) " | City: ${m.nativeCity}" else "")

                    canvas.drawText(line, 45f, y, bodyPaint)
                    y += 18f

                    if (y > pageHeight - 60) {
                        // Overflow guard
                        y += 10f
                    }
                }
                y += 12f
            }

            // Draw Watermark / Footer
            if (!isPremium) {
                canvas.drawText("SHAJRA FREE WATERMARK", 100f, (pageHeight - 100).toFloat(), watermarkPaint)
            } else {
                val footerPaint = Paint().apply {
                    color = Color.rgb(100, 100, 100)
                    textSize = 10f
                }
                canvas.drawText("✨ Certified Shajra PDF • Premium Verified Document", 40f, (pageHeight - 30).toFloat(), footerPaint)
            }

            pdfDocument.finishPage(page)

            // Save PDF File
            val pdfDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.cacheDir
            if (!pdfDir.exists()) pdfDir.mkdirs()

            val pdfFile = File(pdfDir, "Shajra_Nasab_${System.currentTimeMillis()}.pdf")
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()

            // Open or Share File Intent
            val fileUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(Intent.createChooser(intent, "Shajra PDF Kholain Ya Share Karein"))
            Toast.makeText(context, "PDF Ban Gaya! Location: ${pdfFile.name}", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "PDF Export me masla aya: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
