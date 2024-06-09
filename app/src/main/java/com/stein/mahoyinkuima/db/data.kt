package com.stein.mahoyinkuima.db

import androidx.lifecycle.ViewModel
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.*

@Entity(tableName = "message")
@Serializable
data class Message(
        @PrimaryKey val Time: Int,
        @ColumnInfo(name = "topic") val Topic: String,
        @ColumnInfo(name = "name") val Name: String,
        @ColumnInfo(name = "value") val Value: String
)

@Entity(tableName = "key_table")
@Serializable
data class Key(
        @PrimaryKey val Key: String,
)

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM message ORDER BY Time asc") fun getAll(): Flow<List<Message>>

    @Query("SELECT * FROM key_table") fun key(): Flow<List<Key>>

    @Insert suspend fun insertMessage(vararg message: Message)

    @Insert suspend fun insertKey(vararg key: Key)

    @Query("DELETE FROM key_table") suspend fun deleteKey()
}

@Database(entities = [Message::class, Key::class], version = 1, exportSchema = false)
abstract class ChatHistoryDataBase : RoomDatabase() {
    abstract fun chatHistory(): ChatHistoryDao
}

class ChatHistoryModel(private val dao: ChatHistoryDao) : ViewModel() {
    fun getAllMessages(): Flow<List<Message>> {
        return dao.getAll()
    }

    fun getKey(): Flow<List<Key>> {
        return dao.key()
    }

    fun insertMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch { dao.insertMessage(message) }
    }

    fun updateKey(key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteKey()
            dao.insertKey(Key(Key = key))
        }
    }
}
