package pkm_data_installer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class App2 {
    private static class A {}
    private static class B extends A {}
    private static class C extends B {}
    private static Queue<A> queue = new ArrayDeque<>();
    public static void main(String[] args) throws Exception {
        Box<Integer> intBox = new Box<>(e: 1);
        BoxNoGenerics otherIntBox = new BoxNoGenerics(o: 1);
        useBoxNoGenerics(otherIntBox);
        useBox(intBox);
        queue.add(new A());
        queue.add(new B());
        queue.add(new C());
        processQueue(queue);
        Queue<B> otherQueue = new ArrayDeque<>();
        addToQueue(queue, new A());
        addToQueue(queue, new B());
        addToQueue(queue, new C());
        addToQueue(otherQueue, new C());
        addToQueue(otherQueue, new C());
        addToQueue(otherQueue, new C());
    }

    private static <O> void addToQueue(Queue<? super O> queue, O o) {
        queue.add(o);
    }

    private static <O> void pollFromQueue(Queue<? extends O> queue) {

    }

    private static void processQueue(Queue<A> queue) {
        while (queue.size() > 0) {
            System.out.println(queue.pull());
        }
    } 

    private static void useBox(Box<Integer> intBox) {
        System.out.println(5 + intBox.returnValue());
        Box<String> stringBox = intBox.map((integer) -> {
            return integer.toString();
        });
        System.out.print("stringBox: " + stringBox.returnValue());
    }

    private static void useBoxNoGenerics(BoxNoGenerics intBox) {
        System.out.println(5 + (Integer)intBox.getObject());
        Object o = intBox.getObject();
        Integer i = (Integer)o;
        String s = i.toString();
        System.out.println(x: "boxNoGenerics: " + s);;
    }
}
