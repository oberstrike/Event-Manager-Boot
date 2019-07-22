package com.agil.converter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public abstract class DTOConverter {

	@SuppressWarnings("unused")
	private DTOConverter() {

	}

	private Class<?> entityClass;

	private Class<?> dtoClass;

	private Predicate<Field> fieldHasName(String name) {
		return (each) -> each.getName().equals(name);
	}

	private Consumer<Field> setAccessible = (field) -> field.setAccessible(true);

	private Predicate<String> equals(String arg) {
		return (string) -> string.equals(arg);
	}

	public DTOConverter(Class<?> entityClass, Class<?> dtoClass) {
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}

	private boolean containsFieldWithName(List<Field> fields, String name) {
		return fields.stream().map(Field::getName).anyMatch(equals(name));
	}

	private Object getValue(List<Field> fields, String name, Object obj) {
		return fields.stream().filter(fieldHasName(name)).map(each -> {
			try {
				return each.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}).findFirst().get();
	}

	private void setValue(List<Field> fields, String fieldName, Object obj, Object value) {
		try {
			Field field = fields.stream().filter(fieldHasName(fieldName)).findFirst().get();
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private Object convertObject(Object object) {
		Object newObject = null;
		try {
			if (object.getClass().equals(getEntityClass()))
				newObject = dtoClass.newInstance();
			else if (object.getClass().equals(getDtoClass()))
				newObject = entityClass.newInstance();
			else
				throw new Exception("Object has wrong type: " + object.getClass().getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		List<Field> oldFields = Arrays.asList(object.getClass().getDeclaredFields());
		List<Field> newFields = Arrays.asList(newObject.getClass().getDeclaredFields());

		oldFields.forEach(this.setAccessible);
		newFields.forEach(this.setAccessible);

		for (Field field : oldFields) {
			String name = field.getName();

			if (containsFieldWithName(newFields, name)) {
				Object value = getValue(oldFields, name, object);
				setValue(newFields, name, newObject, value);
			}
		}
		return newObject;

	}

	public Object convert(Object object) {
		return this.convertObject(object);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<?> getDtoClass() {
		return dtoClass;
	}

	public void setDtoClass(Class<?> dtoClass) {
		this.dtoClass = dtoClass;
	}

}
