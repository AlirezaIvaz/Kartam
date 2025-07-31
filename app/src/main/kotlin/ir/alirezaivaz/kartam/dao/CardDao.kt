package ir.alirezaivaz.kartam.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.alirezaivaz.kartam.dto.CardInfo

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    suspend fun getAll(): List<CardInfo>

    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun getCard(id: Int): CardInfo?

    @Insert
    suspend fun insert(cardInfo: CardInfo)

    @Update
    suspend fun update(cardInfo: CardInfo)

    @Delete
    suspend fun delete(cardInfo: CardInfo)
}
