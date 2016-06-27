package ru.alfabank.dmpr.model;

import java.io.Serializable;

/**
 * Базовый для всех бизнес-объектов поднимаемых из БД, не имеющих identity
 * Serializable необходим MyBatis для возврата клонов объектов из кэша
 */
public abstract class BaseItem implements Serializable {
}
