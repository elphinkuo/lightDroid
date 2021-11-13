package com.elphin.framework.app.fpstack;

import java.util.Stack;

/**
 * stack with the ability to reorder
 *
 * <p>
 * If there is an element to be pushed, move the current element to the top of the stack, there is no duplicated element in the stack 
 * </p>
 * @author elphin
 * @version 1.0
 * @date 13-6-18 12:12pm
 */
class ReorderStack<T> extends Stack<T> {

    @Override
    public T push(T object) {

        if(!this.contains(object)) {
            return super.push(object);
        }

        int i,cnt = this.size();

        for( i = cnt -1; i>=0;i--) {
            T obj = this.get(i);
            if(object.equals(obj)){
                remove(i);
                break;
            }
        }
        cnt = this.size();
        if(i>=1 && i<cnt) {
            if(get(i-1).equals(get(i))) {
                remove(i);
            }
        }


        return super.push(object);
    }
}
