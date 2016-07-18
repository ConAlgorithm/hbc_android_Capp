package com.hugboga.custom.data.bean;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "line_hot_spot")
public class LineHotSpotBean {

    @Column(name = "type")
    public int type;
    @Column(name = "spot_id", isId = true)
    public int spot_id;
    @Column(name = "spot_name")
    public String spot_name;
    @Column(name = "rank")
    public int rank;
    @Column(name = "hot_weight")
    public int hot_weight;

}
