# Toy-aop
一个简易AOP框架，可以不侵入的拦截指定类的指定方法，再前后和异常织入相应的切面
### 注解
1. @AopBean(pointClass="cutBean")
2. @AopBefore(pointCut="cutMethod")
3. @AopAfter(pointCut="cutMethod")
4. @AopException(pointCut="cutMethod")
### 使用示例
普通类
```java
public class Normal {
    public void hello() {
        System.out.println("hello");
        throw new RuntimeException();
    }
    public void world() {
        System.out.println("world");
    }
}
```
切入类
```java
@AopBean(pointClass = "com.edfeff.aop.test.Normal")
public class TestBean {

    @AopBefore(pointCut = "hello")
    public void before() {
        System.out.println("===========before==============");
    }

    @AopAfter(pointCut = "hello")
    public void after() {
        System.out.println("==========after===============");
    }

    @AopException(pointCut = "hello")
    public void handleException() {
        System.out.println("===========exception==============");
    }

    @AopBefore(pointCut = "world")
    public void beforeWorld() {
        System.out.println("===========before==============");
    }
}
```
示例
```java
public void test(){
    Aop aop = new Aop();
    aop.registerToIoc(TestBean.class);
    aop.registerToIoc(Normal.class);
    //直接从AOP中获取即可，已经被装配好了的bean
    Normal normal = aop.getBeanOfType(Normal.class);
    normal.hello();
    normal.world();
}
```