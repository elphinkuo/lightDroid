package com.elphin.framework.app.fpstack;

import java.util.Stack;

/**
 * 可重新排序的stack
 *
 * <p>
 * 如果栈中已有需要push的元素，把已有的元素移动到栈顶，栈中不存在重复元素。
 * </p>
 * @author elphin
 * @version 1.0
 * @date 13-6-18 下午12:12
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
