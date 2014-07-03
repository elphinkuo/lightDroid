package com.elphin.framework.app.fpstack;

import com.baidu.map.components.platform.manager.IComConsts;
import com.baidu.map.components.platform.manager.utils.PreferenceUtils;

/**
 * 页面类型，区分地图和组件页面 User: elphin Date: 13-8-5 Time: 下午1:38 Add By Component
 * platform
 */
public enum PageCategory {
    MAP,        // 基线页面所属类型
    SCENERY(IComConsts.COM_CATEGORY.SCENERY),    // 景点页面所属类型
    TAXI(IComConsts.COM_CATEGORY.TAXI);       // 打车页面所属类型

    public boolean isComponentCategory() {
        return this != MAP;
    }

    private String categoryName;

    private PageCategory() {
        this.categoryName = "map";
    }

    private PageCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public static PageCategory create(String categoryName) {
        if (IComConsts.COM_CATEGORY.SCENERY.equalsIgnoreCase(categoryName)) {
            return SCENERY;
        } else if (IComConsts.COM_CATEGORY.TAXI.equalsIgnoreCase(categoryName)) {
            return TAXI;
        }

        return MAP;
    }

    public boolean isPlaceCategory() {
        return this == SCENERY;
    }

}
