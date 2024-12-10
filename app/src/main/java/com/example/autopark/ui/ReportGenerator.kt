package com.example.autopark.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.dto.JournalDto
import com.example.autopark.dto.RoutesDto
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.util.Locale

fun generateReport(data: List<Any>, context: Context, reportType: String) {
    when (reportType) {
        "PDF" -> generatePdfReport(data, context)
        "TXT" -> generateTxtReport(data, context)
    }
}

fun generatePdfReport(data: List<Any>, context: Context) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val paint = Paint().apply { textSize = 16f }
    page.canvas.drawText("Report", 100f, 100f, paint)

    var yPosition = 150f
    when (data.firstOrNull()) {
        is AutoDto -> data.forEach {
            val auto = it as AutoDto
            page.canvas.drawText(
                "Num: ${auto.num}, Mark: ${auto.mark}, Color: ${auto.color}",
                100f,
                yPosition,
                paint
            )
            yPosition += 20f
        }

        is JournalDto -> data.forEach {
            val journal = it as JournalDto
            page.canvas.drawText(
                "Time Out: ${journal.timeOut}, Time In: ${journal.timeIn}, Route: ${journal.routeId}, Auto: ${journal.autoId}",
                100f,
                yPosition,
                paint
            )
            yPosition += 20f
        }

        is AutoPersonnelDto -> data.forEach {
            val personnel = it as AutoPersonnelDto
            page.canvas.drawText(
                "First Name: ${personnel.lastName}, Father Name: ${personnel.fatherName}, Last Name: ${personnel.lastName}",
                100f,
                yPosition,
                paint
            )
            yPosition += 20f
        }

        is RoutesDto -> data.forEach {
            val route = it as RoutesDto
            page.canvas.drawText("Route Name: ${route.name}", 100f, yPosition, paint)
            yPosition += 20f
        }
    }

    pdfDocument.finishPage(page)

    val fileName = when (data.firstOrNull()) {
        is AutoDto -> "report_auto.pdf"
        is JournalDto -> "report_journal.pdf"
        is AutoPersonnelDto -> "report_auto_personnel.pdf"
        is RoutesDto -> "report_routes.pdf"
        else -> "report.pdf"
    }

    val pdfFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
    try {
        FileOutputStream(pdfFile).use { outputStream ->
            pdfDocument.writeTo(outputStream)
            downloadFile(context, pdfFile)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        pdfDocument.close()
    }
}

fun generateTxtReport(data: List<Any>, context: Context) {
    val fileName = when (data.firstOrNull()) {
        is AutoDto -> "report_auto.txt"
        is JournalDto -> "report_journal.txt"
        is AutoPersonnelDto -> "report_auto_personnel.txt"
        is RoutesDto -> "report_routes.txt"
        else -> "report.txt"
    }

    val txtFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
    FileWriter(txtFile).use { writer ->
        writer.append("Report\n")
        when (data.firstOrNull()) {
            is AutoDto -> data.forEach {
                val auto = it as AutoDto
                writer.append("Num: ${auto.num}, Mark: ${auto.mark}, Color: ${auto.color}\n")
            }

            is JournalDto -> data.forEach {
                val journal = it as JournalDto
                writer.append("Time Out: ${journal.timeOut}, Time In: ${journal.timeIn}, Route: ${journal.routeId}, Auto: ${journal.autoId}\n")
            }

            is AutoPersonnelDto -> data.forEach {
                val personnel = it as AutoPersonnelDto
                writer.append("First Name: ${personnel.lastName}, Father Name: ${personnel.fatherName}, Last Name: ${personnel.lastName}\n")
            }

            is RoutesDto -> data.forEach {
                val route = it as RoutesDto
                writer.append("Route Name: ${route.name}\n")
            }
        }
        downloadFile(context, txtFile)
    }
}

fun downloadFile(context: Context, file: File) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver = context.contentResolver
        val uri: Uri? =
            contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().copyTo(outputStream)
            }
            openFile(context, it)
        }
    } else {
        val destinationFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            file.name
        )
        file.copyTo(destinationFile, overwrite = true)
        openFile(context, Uri.fromFile(destinationFile))
    }
}

fun openFile(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        val mimeType = when (uri.toString().substringAfterLast('.').lowercase(Locale.ROOT)) {
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "csv" -> "text/csv"
            else -> "*/*"
        }
        setDataAndType(uri, mimeType)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(intent, "Open Report"))
}

@Composable
fun ReportTypeDialog(onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    val reportTypes = listOf("PDF", "TXT")

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Report Type",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                reportTypes.forEach { type ->
                    Button(
                        onClick = {
                            onSelect(type)
                            onDismiss()
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = type,
                            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        dismissButton = null
    )
}

@Composable
fun DownloadReportButton(list: List<Any>, context: Context) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedReportType = remember { mutableStateOf("PDF") }

    Button(
        onClick = { showDialog.value = true },
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(16.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = "Download Report", color = androidx.compose.ui.graphics.Color.White)
    }

    if (showDialog.value) {
        ReportTypeDialog(
            onDismiss = { showDialog.value = false },
            onSelect = { type ->
                selectedReportType.value = type
                showDialog.value = false
                generateReport(list, context, selectedReportType.value)
            }
        )
    }
}
