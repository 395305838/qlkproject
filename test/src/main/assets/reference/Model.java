package reference;

import java.io.Serializable;

public class Model implements Comparable, Serializable {

	private static final long serialVersionUID = -4805234522634569840L;

	private String name;
	private int age;

	public Model() {
		super();
	}

	public Model(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	/**
	 * 字符串已经重写了hashcode和equals
	 */
	@Override
	public int hashCode() {
		return name.hashCode() + age * 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Model)) {
			throw new ClassCastException("类型错误");
		}
		Model stu = (Model) obj;
		return this.name.equals(stu.name) && this.age == stu.age;
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof Model)) {
			throw new ClassCastException("类型不符合要求");
		}
		Model stu = (Model) o;
		int temp = this.age - stu.age;
		return temp == 0 ? this.name.compareTo(stu.name) : temp;
	}

	@Override
	public String toString() {
		return "Model [name=" + name + ", age=" + age + "]";
	}

}
