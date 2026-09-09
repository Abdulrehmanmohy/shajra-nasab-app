package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FamilyRepository(private val dao: FamilyMemberDao) {

    val allMembers: Flow<List<FamilyMember>> = dao.getAllMembers()
    val totalCount: Flow<Int> = dao.getMemberCount()

    suspend fun insert(member: FamilyMember) = dao.insertMember(member)

    suspend fun update(member: FamilyMember) = dao.updateMember(member)

    suspend fun delete(member: FamilyMember) = dao.deleteMember(member)

    suspend fun deleteById(id: Int) = dao.deleteMemberById(id)

    suspend fun seedInitialTreeIfEmpty() {
        val count = dao.getMemberCount().first()
        if (count == 0) {
            val sampleTree = listOf(
                FamilyMember(
                    name = "Mirza Ghulam Muhammad Khan",
                    fatherName = "Mirza Fateh Ali Khan",
                    parentId = null,
                    generation = 1,
                    relation = "Pardada / Great Grandfather",
                    gender = "Male",
                    birthYear = "1895 - 1970",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Lahore",
                    profession = "Landowner & Scholar",
                    notes = "Family patriarch who established our ancestral estate in Lahore.",
                    isLiving = false
                ),
                FamilyMember(
                    name = "Mirza Abdul Samad Khan",
                    fatherName = "Mirza Ghulam Muhammad Khan",
                    parentId = 1,
                    generation = 2,
                    relation = "Dada Ji / Grandfather",
                    gender = "Male",
                    birthYear = "1928 - 2005",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Rawalpindi",
                    profession = "Civil Engineer",
                    notes = "Served in public works department, built several historic bridges.",
                    isLiving = false
                ),
                FamilyMember(
                    name = "Begum Rasheeda Akhtar",
                    fatherName = "Syed Ahmad Shah",
                    parentId = 1,
                    generation = 2,
                    relation = "Dadi Ji / Grandmother",
                    gender = "Female",
                    birthYear = "1935 - 2018",
                    casteTribe = "Syed",
                    nativeCity = "Multan",
                    profession = "Homemaker",
                    notes = "Known for her hospitality and poetry collections.",
                    isLiving = false
                ),
                FamilyMember(
                    name = "Mirza Tariq Mahmood Khan",
                    fatherName = "Mirza Abdul Samad Khan",
                    parentId = 2,
                    generation = 3,
                    relation = "Abba Ji / Father",
                    gender = "Male",
                    birthYear = "1960",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Islamabad",
                    profession = "Professor of Economics",
                    phone = "+92 300 1234567",
                    notes = "Senior faculty member and author.",
                    isLiving = true
                ),
                FamilyMember(
                    name = "Ayesha Begum",
                    fatherName = "Chaudhry Muhammad Yousaf",
                    parentId = 2,
                    generation = 3,
                    relation = "Ammi Ji / Mother",
                    gender = "Female",
                    birthYear = "1965",
                    casteTribe = "Chaudhry / Jatt",
                    nativeCity = "Faisalabad",
                    profession = "Educationist",
                    notes = "School principal and community social worker.",
                    isLiving = true
                ),
                FamilyMember(
                    name = "Mirza Abdul Rehman Khan",
                    fatherName = "Mirza Tariq Mahmood Khan",
                    parentId = 4,
                    generation = 4,
                    relation = "Self / Aap",
                    gender = "Male",
                    birthYear = "1992",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Islamabad",
                    profession = "Software Architect",
                    phone = "+92 321 9876543",
                    notes = "Creator of Shajra-e-Nasab digital family tree application.",
                    isLiving = true
                ),
                FamilyMember(
                    name = "Mirza Hammad Ali",
                    fatherName = "Mirza Tariq Mahmood Khan",
                    parentId = 4,
                    generation = 4,
                    relation = "Bhai / Brother",
                    gender = "Male",
                    birthYear = "1996",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Lahore",
                    profession = "Chartered Accountant",
                    isLiving = true
                ),
                FamilyMember(
                    name = "Zainab Mirza",
                    fatherName = "Mirza Tariq Mahmood Khan",
                    parentId = 4,
                    generation = 4,
                    relation = "Behan / Sister",
                    gender = "Female",
                    birthYear = "2000",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Islamabad",
                    profession = "Graphic Designer",
                    isLiving = true
                ),
                FamilyMember(
                    name = "Mirza Muhammad Azan",
                    fatherName = "Mirza Abdul Rehman Khan",
                    parentId = 6,
                    generation = 5,
                    relation = "Beta / Son",
                    gender = "Male",
                    birthYear = "2021",
                    casteTribe = "Mirza / Mughal",
                    nativeCity = "Islamabad",
                    profession = "Student",
                    isLiving = true
                )
            )
            dao.insertAll(sampleTree)
        }
    }
}
