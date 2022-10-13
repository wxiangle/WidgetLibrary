package org.aaron.widgetlibrary

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_expand_text_test.*

/**
 * Created by wangxl1 on 2022/10/10 16:35
 * E-Mail Address： wang_x_le@163.com
 */
class ExpandTextTestActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expand_text_test)

        scroll_expand_text_view?.setTextColor(Color.parseColor("#000000"))
        scroll_expand_text_view?.setContent("天有不测风云，展开全文 收起 人有旦夕祸福。蜈蚣百足，行不及蛇；雄鸡两翼，飞不过鸦。马有千里之程，无骑不能自往；人有冲天之志，非运不能自通。\n" +
                "\n" +
                " \n" +
                "\n" +
                "盖闻：人生在世，富贵不能淫，贫贱不能移。文章盖世，孔子厄于陈邦；武略超群，太公钓于渭水。颜渊命短，殊非凶恶之徒；盗跖年长，岂是善良之辈。尧帝明圣，却生不肖之儿；瞽叟愚顽，反生大孝之子。张良原是布衣，萧何称谓县吏。晏子身无五尺，封作齐国宰相；孔明卧居草庐，能作蜀汉军师。楚霸虽雄，败于乌江自刎；汉王虽弱，竟有万里江山。李广有射虎之威，到老无封；冯唐有乘龙之才，一生不遇。韩信未遇之时，无一日三餐，及至遇行，腰悬三尺玉印，一旦时衰，死于阴人之手。\n" +
                "\n" +
                " \n" +
                "\n" +
                "有先贫而后富，有老壮而少衰。满腹文章，白发竟然不中；才疏学浅，少年及第登科。深院宫娥，运退反为妓妾；风流妓女，时来配作夫人。 \n" +
                "\n" +
                "青春美女，却招愚蠢之夫；俊秀郎君，反配粗丑之妇。蛟龙未遇，潜水于鱼鳖之间；君子失时，拱手于小人之下。衣服虽破，常存仪礼之容；面带忧愁，每抱怀安之量。时遭不遇，只宜安贫守份；心若不欺，必然扬眉吐气。初贫君子，天然骨骼生成；乍富小人，不脱贫寒肌体。\n" +
                "\n" +
                " \n" +
                "\n" +
                "天不得时，日月无光；地不得时，草木不生；水不得时，风浪不平；人不得时，利运不通。注福注禄，命里已安排定，富贵谁不欲？人若不依根基八字，岂能为卿为相？\n" +
                "\n" +
                " \n" +
                "\n" +
                "吾昔寓居洛阳，朝求僧餐，暮宿破窖，思衣不可遮其体，思食不可济其饥，上人憎，下人厌，人道我贱，非我不弃也。今居朝堂，官至极品，位置三公，身虽鞠躬于一人之下，而列职于千万人之上，有挞百僚之杖，有斩鄙吝之剑，思衣而有罗锦千箱，思食而有珍馐百味，出则壮士执鞭，入则佳人捧觞，上人宠，下人拥。人道我贵，非我之能也，此乃时也、运也、命也。\n" +
                "\n" +
                " \n" +
                "\n" +
                "嗟呼！人生在世，富贵不可尽用，贫贱不可自欺，听由天地循环，周而复始焉。展开全文 收起" )
    }
}