package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FamilyDatabase
import com.example.data.FamilyMember
import com.example.data.FamilyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FamilyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FamilyRepository
    val searchQuery = MutableStateFlow("")
    val selectedGenFilter = MutableStateFlow<Int?>(null)

    init {
        val database = FamilyDatabase.getDatabase(application)
        repository = FamilyRepository(database.familyMemberDao())
        
        viewModelScope.launch {
            repository.seedInitialTreeIfEmpty()
        }
    }

    val allMembers: StateFlow<List<FamilyMember>> = repository.allMembers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalMembersCount: StateFlow<Int> = repository.totalCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val filteredMembers: StateFlow<List<FamilyMember>> = combine(
        allMembers,
        searchQuery,
        selectedGenFilter
    ) { members, query, gen ->
        members.filter { member ->
            val matchesQuery = query.isBlank() ||
                    member.name.contains(query, ignoreCase = true) ||
                    member.fatherName.contains(query, ignoreCase = true) ||
                    member.casteTribe.contains(query, ignoreCase = true) ||
                    member.nativeCity.contains(query, ignoreCase = true) ||
                    member.relation.contains(query, ignoreCase = true)
            
            val matchesGen = gen == null || member.generation == gen
            matchesQuery && matchesGen
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.insert(member)
        }
    }

    fun updateMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.update(member)
        }
    }

    fun deleteMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.delete(member)
        }
    }

    fun deleteMemberById(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setGenFilter(gen: Int?) {
        selectedGenFilter.value = gen
    }

    fun getLineageChain(member: FamilyMember, members: List<FamilyMember>): String {
        val chain = mutableListOf<String>()
        var current: FamilyMember? = member
        val visited = mutableSetOf<Int>()

        while (current != null && !visited.contains(current.id)) {
            visited.add(current.id)
            chain.add(current.name)
            if (current.parentId != null) {
                current = members.find { it.id == current?.parentId }
            } else if (current.fatherName.isNotBlank()) {
                chain.add(current.fatherName)
                current = null
            } else {
                current = null
            }
        }
        return chain.joinToString(" bin/s/o ")
    }
}
