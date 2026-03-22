package com.loveapp

import android.os.Bundle
import android.app.Activity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loveView = findViewById<LoveEffectView>(R.id.loveView)
        
        val textList = listOf(
            "星星宝宝我爱你！",
            "告诉你一个秘密：|我想和你一辈子在一起",
            "我要一直陪着你",
            "我爱你",
            "你是我的宝藏",
            "你是我的神明",
            "你是我的星星",
            "宝宝！|不开心的时候找我聊天哦！",
            "宝宝！|不可以委屈自己哦！",
            "宝宝！|要微笑",
            "宝宝，|我想你了...",
            "累了吗？|抱抱你...",
            "你一定可以的，|要相信自己！",
            "你是最棒的，|不要伤心和难过哦",
            "李星辉天下第一",
            "我其实也有信仰的，|李星辉是我的神",
            "妈咪我爱你",
            "冷了要多穿衣服哦",
            "下雨天记得带伞",
            "地面滑的时候注意脚下",
            "走夜路注意安全",
            "你是我的心之壁",
            "你是我的缪斯",
            "我喜欢你！|你也喜欢我好嘛...",
            "不知道还能说什么了|总之妈咪你要天天开心！|我爱你！"
        )
        
        loveView.setTextList(textList)
        loveView.setRandomText()
    }
}
