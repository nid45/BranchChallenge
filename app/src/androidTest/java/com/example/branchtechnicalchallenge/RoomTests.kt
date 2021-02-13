package com.example.branchtechnicalchallenge

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.db.listsdb.ListsDAO
import com.example.branchtechnicalchallenge.db.Database
import com.example.branchtechnicalchallenge.db.tododb.ToDoDAO
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var listDao: ListsDAO
    private lateinit var todoDao: ToDoDAO
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, Database::class.java).build()
        listDao = db.listsDAO()!!
        todoDao = db.todoDAO()!!

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun writeListAndReadInList() {
        val list = Lists("test list", System.currentTimeMillis())
        list.uid = listDao.addList(list)
        val byUid = listDao.getList(list.uid)
        if (byUid != null) {
            assertThat(list.uid, equalTo(byUid.uid))
        }
    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testUpdate() {
        val list = Lists("test list", System.currentTimeMillis())
        list.uid = listDao.addList(list)
        list.title = "new title"
        listDao.updateLists(list)
        val byUid = listDao.getList(list.uid)
        assertEquals("new title".trim(), byUid!!.title.trim())
    }


    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testDelete() {
        val list = Lists("test list", System.currentTimeMillis())
        val list2 = Lists("test list2", System.currentTimeMillis())
        var uid1 = listDao.addList(list)
        var uid2= listDao.addList(list2)
        list.uid = uid1
        listDao.deleteLists(list)
        val listOfLists = listDao.lists
        if (listOfLists != null) {
            assertEquals(1, listOfLists.size)
        }
        if (listOfLists != null) {
            for(l in listOfLists){
                assertEquals("test list2", l.title)
            }
        }
    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testGetAllLists() {
        val list = Lists("test list", System.currentTimeMillis())
        val list2 = Lists("test list2", System.currentTimeMillis())
        list.uid = listDao.addList(list)
        list2.uid= listDao.addList(list2)
        var lists = listDao.lists

        if (lists != null) {
            assertEquals(2, lists.size)
        }

    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun writeListAndToDo() {
        val list = Lists("test list", System.currentTimeMillis())
        list.uid = listDao.addList(list)
        listDao.getList(list.uid)
        val todo = ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        todo.uid = todoDao.addTodo(todo)
        val byUid = todoDao.getTodo(todo.uid)
        if (byUid != null) {
            assertThat(todo.uid, equalTo(byUid.uid))
        }
    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testUpdateTodo() {
        val list = Lists("test list", System.currentTimeMillis())
        list.uid = listDao.addList(list)
        listDao.getList(list.uid)
        val todo = ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        todo.uid = todoDao.addTodo(todo)
        todo.title = "new title"
        todoDao.updateToDo(todo)
        val byUid = todoDao.getTodo(todo.uid)
        if (byUid != null) {
            assertEquals("new title", byUid.title.trim())
        }
    }


    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testDeleteTodo() {
        val list = Lists("test list", System.currentTimeMillis())
        val list2 = Lists("test list2", System.currentTimeMillis())
        list.uid = listDao.addList(list)
        list2.uid = listDao.addList(list2)
        ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        ToDo("test todo", "test desc", System.currentTimeMillis(), list2.uid, false)

        val lists1 = todoDao.getTodoForList(list.uid)
        assertEquals(2, lists1.size)
        val lists2 = todoDao.getTodoForList(list2.uid)
        assertEquals(1, lists2.size)
    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testGetTodosForList() {
        val list = Lists("test list", System.currentTimeMillis())
        val list2 = Lists("test list2", System.currentTimeMillis())
        var uid1 = listDao.addList(list)
        var uid2= listDao.addList(list2)
        list.uid = uid1
        listDao.deleteLists(list)
        val listOfLists = listDao.lists
        if (listOfLists != null) {
            assertEquals(1, listOfLists.size)
        }
        if (listOfLists != null) {
            for(l in listOfLists){
                assertEquals("test list2".trim(), l.title.trim())
            }
        }
    }

    @Test(timeout = 100)
    @Throws(Exception::class)
    fun testCascadeDelete() {
        val list = Lists("test list", System.currentTimeMillis())
        var uid1 = listDao.addList(list)
        list.uid = uid1
        ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        ToDo("test todo", "test desc", System.currentTimeMillis(), list.uid, false)
        listDao.deleteLists(list)
        val listOfLists = listDao.lists
        val listOfToDos = todoDao.getTodoForList(list.uid)
        if (listOfLists != null) {
            assertEquals(0, listOfLists.size)
        }
        if (listOfLists != null) {
            for(l in listOfLists){
                assertEquals("test list2".trim(), l.title.trim())
            }
        }
    }


}