package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val fatherName: String = "",
    val parentId: Int? = null,
    val generation: Int = 1, // 1 = Great Ancestor / Pardada, 2 = Dada, 3 = Abba, 4 = Aulad, 5 = Potay
    val relation: String = "Member", // "Dada / Ancestor", "Father", "Mother", "Self", "Son", "Daughter", "Sibling", "Spouse"
    val gender: String = "Male", // "Male" or "Female"
    val birthYear: String = "",
    val casteTribe: String = "", // Zaat / Khabeela
    val nativeCity: String = "", // Watan / City
    val profession: String = "",
    val phone: String = "",
    val notes: String = "",
    val isLiving: Boolean = true
)
