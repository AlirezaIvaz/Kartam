package ir.alirezaivaz.kartam.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.alirezaivaz.kartam.dto.CardInfo

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY position ASC")
    suspend fun getAll(): List<CardInfo>

    @Query("SELECT COUNT(id) FROM cards")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(id) FROM cards where owned=1")
    suspend fun getOwnedCount(): Int

    @Query("SELECT COUNT(id) FROM cards where owned=0")
    suspend fun getOthersCount(): Int

    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun getCard(id: Int): CardInfo?

    @Query("SELECT MAX(position) FROM cards")
    suspend fun getMaxPosition(): Int?

    @Insert
    suspend fun insert(cardInfo: CardInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardInfo>)

    @Update
    suspend fun update(cardInfo: CardInfo)

    @Delete
    suspend fun delete(cardInfo: CardInfo)
}
