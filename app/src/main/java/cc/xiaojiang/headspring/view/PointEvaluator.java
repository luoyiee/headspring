package cc.xiaojiang.headspring.view;

import android.animation.TypeEvaluator;

/**
 * @author :jinjiafeng
 * date:  on 18-7-10
 * description:
 */
// 实现TypeEvaluator接口
public class PointEvaluator implements TypeEvaluator {

    // 复写evaluate（）
    // 在evaluate（）里写入对象动画过渡的逻辑
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        // 将动画初始值startValue 和 动画结束值endValue 强制类型转换成Point对象
        Point startPoint = (Point) startValue;
        Point endPoint =  (Point)endValue;

        // 根据fraction来计算当前动画的x和y的值
        int x = (int) (startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX()));
        int y = (int) (startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY()));

        // 将计算后的坐标封装到一个新的Point对象中并返回
        return new Point(x, y);
    }

}
