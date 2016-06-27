package ru.alfabank.dmpr.model;

import java.io.Serializable;

/**
 * Базовый для всех Entity - классов экземляров коллекий параметров (фильтров) витрин
 * Serializable необходим MyBatis для возврата клонов объектов из кэша
 */
public class BaseEntity implements Serializable {
    // identity of entity
    public long id;
    public String name;

    // используется материализатором MyBatis
    public BaseEntity() {
    }

    public BaseEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Виртуальный метод создает экземпляр базового типа на основе текущего
     *
     * @return экземпляр базового типа
     */
    public BaseEntity toBaseEntity() {
        return new BaseEntity(id, name);
    }

    /**
     * Хелпер-метод преобразует массив производных типов в массив базового типа
     *
     * @param source массив производных типов
     * @param <T>    производный тип
     * @return массив базового типа
     */
    public static <T extends BaseEntity> BaseEntity[] toBaseEntities(T[] source) {
        BaseEntity[] result = new BaseEntity[source.length];
        for (int i = 0; i < source.length; i++)
            result[i] = source[i].toBaseEntity();
        return result;
    }

    /**
     * Хелпер-метод находит entity по id в массиве
     *
     * @param items массив
     * @param id    identity
     * @param <T>   тип
     * @return entity
     */
    public static <T extends BaseEntity> T getById(T[] items, long id) {
        for (T item : items) {
            if (item.id == id)
                return item;
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (id != that.id) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
