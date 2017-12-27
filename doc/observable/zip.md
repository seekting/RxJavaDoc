# zip

```java
public class ZipDemo {
    public static void main(String args[]) {
        List<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");
        strings.add("c");
        strings.add("d");
        strings.add("e");
        Observable.just(1, 2, 3, 4).zipWith(strings, new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(Integer integer, String s) throws Exception {
                return new Person(s, integer);
            }
        }).subscribe(new MyDisposableObserver("zip:"));
    }

    static class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

```

```Console
zip::Person{name='a', age=1}
zip::Person{name='b', age=2}
zip::Person{name='c', age=3}
zip::Person{name='d', age=4}
zip::onComplete

Process finished with exit code 0

```