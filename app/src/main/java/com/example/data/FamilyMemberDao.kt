package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members ORDER BY generation ASC, id ASC")
    fun getAllMembers(): Flow<List<FamilyMember>>

    @Query("SELECT * FROM family_members WHERE id = :id LIMIT 1")
    suspend fun getMemberById(id: Int): FamilyMember?

    @Query("SELECT * FROM family_members WHERE parentId = :parentId ORDER BY id ASC")
    fun getChildrenOf(parentId: Int): Flow<List<FamilyMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMember): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<FamilyMember>)

    @Update
    suspend fun updateMember(member: FamilyMember)

    @Delete
    suspend fun deleteMember(member: FamilyMember)

    @Query("DELETE FROM family_members WHERE id = :id")
    suspend fun deleteMemberById(id: Int)

    @Query("SELECT COUNT(*) FROM family_members")
    fun getMemberCount(): Flow<Int>
}
