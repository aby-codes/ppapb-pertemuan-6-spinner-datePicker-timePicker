package com.example.pertemuan_7

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pertemuan_7.databinding.ActivityPresensiBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class PresensiActivity : AppCompatActivity() {

    // Deklarasi variabel
    private lateinit var binding: ActivityPresensiBinding
    private var selectedDay = 0
    private var selectedMonth = 0
    private var selectedYear = 0
    private var selectedHour = 0
    private var selectedMinute = 0
    private var selectedAttendancePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memuat array dari resources string
        val months = resources.getStringArray(R.array.months_array)
        val attendanceOptions = resources.getStringArray(R.array.attendance_options_array)

        with(binding) {
            // Mengecek perubahan tanggal yang dipilih
            date.setOnDateChangeListener { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
            }

            // Menyiapkan adapter untuk spinner jenis kehadiran
            val attendanceAdapter = ArrayAdapter(
                this@PresensiActivity,
                android.R.layout.simple_spinner_dropdown_item,
                attendanceOptions
            )
            description.adapter = attendanceAdapter

            // Mengecek pemilihan jenis kehadiran
            description.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedAttendancePosition = position
                        etKeterangan.visibility = if (position > 0) View.VISIBLE else View.GONE
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            // Mengecek perubahan waktu yang dipilih
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
            }

            // Mengatur listener saat tombol "Submit" diklik
            submitButton.setOnClickListener {
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                val selectedMonthString = months[selectedMonth]
                val selectedDate = "$selectedDay $selectedMonthString $selectedYear"
                val currentDate = getCurrentDateTime()

                // Memeriksa apakah input valid
                if (selectedDay == 0) {
                    showToast(getString(R.string.tanggal_kosong))
                } else if (selectedHour == 0) {
                    showToast(getString(R.string.waktu_kosong))
                } else if (selectedAttendancePosition > 0 && etKeterangan.text.toString().isEmpty()) {
                    showToast(getString(R.string.keterangan_kosong))
                } else {
                    showToast("${getString(R.string.presensi_berhasil)} $selectedDate jam $formattedTime")
                }
            }
        }
    }

    // Fungsi untuk mendapatkan tanggal dan waktu saat ini
    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return dateFormat.format(calendar.time)
    }

    // Fungsi untuk menampilkan pesan Toast
    private fun showToast(message: String) {
        Toast.makeText(this@PresensiActivity, message, Toast.LENGTH_SHORT).show()
    }
}
