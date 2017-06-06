package com.example.kotlin_demo.application


interface I {

    interface Goods {
        companion object {
            val KEY_RESULT = "result"
            val RESPONSE_SUCCESS = "success"
            val KEY_INFO = "info"
            val KEY_GOODS = "goods"
            val KEY_CURRENCY_PRICE = "currency_price"
            val KEY_GOODS_ID = "goods_id"
            val KEY_GOODS_NAME = "goods_name"
            val KEY_ENGLISH_NAME = "english_name"

            val HINT_DOWNLOAD_TITLE = "加载商品信息"
            val HINT_DOWNLOADING = "加载中..."
            val HINT_DOWNLOAD_FAILURE = "加载数据失败"
        }
    }

    interface Boutique : Goods {
        companion object {
            val ID = "id"
            val CAT_ID = "catId"
            val TITLE = "title"
            val DESCRIPTION = "description"
            val NAME = "name"
            val IMAGE_URL = "imageurl"
        }
    }

    interface NewGoods : Goods {
        companion object {
            val KEY_THUMB_URL = "thumb"
            val HINT_DOWNLOAD_TITLE = "加载新品列表"
            val HINT_DOWNLOADING = "加载中..."
            val HINT_DOWNLOAD_FAILURE = "加载数据失败"
        }
    }

    interface GoodsDetails : Goods {
        companion object {
            val HINT_DOWNLOAD_TITLE = "加载商品详细信息"
            val KEY_CAT_ID = "cat_id"
            val KEY_ENGLISH_NAME = "goods_english_name"
            val KEY_GOODS_BRIEF = "goods_brief"
            val KEY_GOODS_DESC = "goods_desc"
            val KEY_GOODS_IMG = "goods_img"
            val KEY_GOODS_THUMB = "goods_thumb"
            val KEY_SHOP_PRICE = "shop_price"
            val KEY_PROPERTIES = "properties"
            val KEY_ALBUMS = "albums"
        }
    }

    interface Category : Goods {
        companion object {
            val HINT_DOWNLOAD_TITLE = "加载分类列表"
            val KEY_ID = "id"
            val KEY_NAME = "name"
            val KEY_COLOR_ID = "colorid"
            val KEY_COLOR_NAME = "colorname"

            val KEY_CAT_ID = "cat_id"
            val KEY_CATEGORY_INFO = "category_info"
            val PARAM_PAGE = "&page="
            val PARAM_C_ID = "&c_id="
            val PARAM_CAT_ID = "&cat_id="
            val PARAM_ORDER_PRICE = "&order_price="
            val PARAM_COLOR_ID = "&getcolorid="

            val SORT_DEFAULT = 0// 排序默认值
            val SORT_PRICE_ASC = 1// 价格升序排序
            val SORT_PRICE_DESC = 2// 价格降序排序
            val SORT_DATE_ASC = 3// 日期升序排序
            val SORT_DATE_DESC = 4// 日期降序排序

            val COLOR_DEFAULT = -1// 排序默认值
        }
    }

    interface NewAndBoutiqueGoods {
        companion object {
            val CAT_ID = "cat_id"
            /** 颜色id */
            val COLOR_ID = "color_id"
            /** 颜色名 */
            val COLOR_NAME = "color_name"
            /** 颜色代码 */
            val COLOR_CODE = "color_code"
            /** 导购链接 */
            val COLOR_URL = "color_url"
        }
    }

    interface CategoryGroup {
        companion object {
            val ID = "id"
            val NAME = "name"
            val IMAGE_URL = "imageurl"
        }
    }

    interface CategoryChild : CategoryGroup {
        companion object {
            val PARENT_ID = "parent_id"
            val CAT_ID = "catId"
            val DATA = "category_child_data"
        }
    }

    interface CategoryGood {
        companion object {
            val TABLE_NAME = "tb_category_good"
            val ID = "id"
            /** 商品id */
            val GOODS_ID = "goods_id"
            /** 所属类别的id */
            val CAT_ID = "cat_id"
            /** 商品的中文名称 */
            val GOODS_NAME = "goods_name"
            /** 商品的英文名称 */
            val GOODS_ENGLISH_NAME = "goods_english_name"
            /** 商品简介 */
            val GOODS_BRIEF = "goods_brief"
            /** 商品原始价格 */
            val SHOP_PRICE = "shop_price"
            /** 商品的RMB价格  */
            val CURRENT_PRICE = "currency_price"
            /** 商品折扣价格  */
            val PROMOTE_PRICE = "promote_price"
            /** 人民币折扣价格 */
            val RANK_PRICE = "rank_price"
            /**是否折扣 */
            val IS_PROMOTE = "is_promote"
            /** 商品缩略图地址 */
            val GOODS_THUMB = "goods_thumb"
            /** 商品图片地址 */
            val GOODS_IMG = "goods_img"
            /** 分享地址 */
            val ADD_TIME = "add_time"
            /** 分享地址 */
            val SHARE_URL = "share_url"
        }
    }

    interface Property {
        companion object {
            val ID = "id"
            val goodsId = "goods_id"
            val COLOR_ID = "colorid"
            val COLOR_NAME = "colorname"
            val COLOR_CODE = "colorcode"
            val COLOR_IMG = "colorimg"
            val COLOR_URL = "colorurl"
        }
    }

    interface Album {
        companion object {
            val TABLE_NAME = "tb_album"
            val ID = "id"
            val PID = "pid"
            val IMG_ID = "img_id"
            val IMG_URL = "img_url"
            val THUMB_URL = "thumb_url"
            val IMG_DESC = "img_desc"
        }
    }

    interface Cart {
        companion object {
            val ID = "id"
            val GOODS_ID = "goods_id"
            val GOODS_THUMB = "goodsThumb"
            val USER_NAME = "userName"
            /**购物车中该商品的件数 */
            val COUNT = "count"
            /**商品是否已被选中 */
            val IS_CHECKED = "isChecked"
            val PAY_PRICE = "payPrice"
        }
    }

    interface Collect {
        companion object {
            /** 商品id */
            val ID = "id"
            val GOODS_ID = "goods_id"
            val USER_NAME = "userName"
            /** 商品的中文名称 */
            val GOODS_NAME = "goodsName"
            /** 商品的英文名称 */
            val GOODS_ENGLISH_NAME = "goodsEnglishName"
            val GOODS_THUMB = "goodsThumb"
            val GOODS_IMG = "goodsImg"
            val ADD_TIME = "addTime"
        }
    }

    interface User {
        companion object {
            val TABLE_NAME = "t_superwechat_user"
            val USER_ID = "m_user_id"//主键
            val USER_NAME = "m_user_name"//用户账号
            val PASSWORD = "m_user_password"//用户密码
            val NICK = "m_user_nick"//用户昵称
            val UN_READ_MSG_COUNT = "m_user_unread_msg_count"//未读消息数量
        }
    }

    interface Contact {
        companion object {
            val TABLE_NAME = "t_superwechat_contact"
            val CONTACT_ID = "m_contact_id"//主键
            val USER_ID = "m_contact_user_id"//用户id
            val USER_NAME = "m_contact_user_name"//用户账号
            val CU_ID = "m_contact_cid"//好友id
            val CU_NAME = "m_contact_cname"//好友账号
        }
    }

    interface Avatar {
        companion object {
            val TABLE_NAME = "t_superwechat_avatar"
            val AVATAR_ID = "m_avatar_id"//主键
            val USER_ID = "m_avatar_user_id"//用户id或者群组id
            val USER_NAME = "m_avatar_user_name"//用户账号或者群组账号
            val AVATAR_PATH = "m_avatar_path"//保存路径
            val AVATAR_TYPE = "m_avatar_type"//头像类型：\n0:用户头像\n1:群组头像
        }
    }

    companion object {
        val SERVER_ROOT = "http://101.251.196.90:8080/FuLiCenterServerV2.0/"
        /** 第一次下载 */
        val ACTION_DOWNLOAD = 0
        /** 下拉刷新 */
        val ACTION_PULL_DOWN = 1
        /** 上拉刷新 */
        val ACTION_PULL_UP = 2

        /** 每行显示的数量columNum */
        val COLUM_NUM = 2


        /** 表示列表项布局的两种类型 */
        val TYPE_ITEM = 0
        val TYPE_FOOTER = 1

        val REQUEST_CODE_REGISTER = 101
        val REQUEST_CODE_LOGIN = 102
        val REQUEST_CODE_NICK = 103
        val REQUEST_CODE_LOGIN_FROM_CART = 104

        /** BeeColud APP ID  */
        val BEE_COLUD_APP_ID = "3539b590-4859-4128-87a3-5fb8b86b94f6"
        /** BeeColud APP Secret */
        val BEE_COLUD_APP_SECRET = "c75c74e1-105e-437c-9be9-84c4ddee4d5f"
        /** BeeColud APP Test Secret */
        val BEE_COLUD_APP_SECRET_TEST = "06eb1210-0eeb-41df-99e3-1ffb9eb87b99"
        /** weixin APP ID  */
        val WEIXIN_APP_ID = "wxf1aa465362b4c8f1"
        // 如果使用PayPal需要在支付之前设置client id和应用secret
        val PAYPAL_CLIENT_ID = "AVT1Ch18aTIlUJIeeCxvC7ZKQYHczGwiWm8jOwhrREc4a5FnbdwlqEB4evlHPXXUA67RAAZqZM0H8TCR"
        val PAYPAL_SECRET = "EL-fkjkEUyxrwZAmrfn46awFXlX-h2nRkyCVhhpeVdlSRuhPJKXx3ZvUTTJqPQuAeomXA8PZ2MkX24vF"

        //商户名称
        val MERCHANT_NAME = "福利社"

        //货币单位
        val CURRENCY_TYPE_CNY = "CNY"
        val CURRENCY_TYPE_USD = "USD"

        val TEXT_HTML = "text/html"

        val ACTION_TYPE_PERSONAL = "personal"
        val ACTION_TYPE_CART = "cart"

        /** 添加收藏 */
        val ACTION_ADD_COLLECT = 1
        /** 取消收藏 */
        val ACTION_DELETE_COLLECT = 2

        val NEW_GOOD = 0
        val CATEGORY_GOOD = 1
        val CAT_ID = 0
        val BROADCAST_UPDATA_CART = "cn.ucai.fulicenter.update.cart"
        val CART_CHECKED_DEFAULT = 0
        val BROADCAST_UPDATA_COLLECT = "cn.ucai.fulicenter.update.collect"
        val ACTION_CART_ADD = 1
        val ACTION_CART_DEL = 2
        val ACTION_CART_UPDATA = 3
        val REQUEST_CODE_COLLECT = 105
        val ACTION_ADD_CART = 1
        val ACTION_DELETE_CART = -1

        /**
         * 商品排序方式
         */
        val SORT_BY_PRICE_ASC = 1
        val SORT_BY_PRICE_DESC = 2
        val SORT_BY_ADDTIME_ASC = 3
        val SORT_BY_ADDTIME_DESC = 4
        val ISON8859_1 = "iso8859-1"
        val UTF_8 = "utf-8"
        val PAGE_ID = "page_id"//分页的起始下标
        val PAGE_SIZE = "page_size"//分页的每页数量
        val PAGE_ID_DEFAULT = 1//分页的起始下标默认值
        val PAGE_SIZE_DEFAULT = 10//分页的每页数量默认值

        val ID_DEFAULT = 0//ID默认值
        val UN_READ_MSG_COUNT_DEFAULT = 0//未读消息数量默认值
        val GROUP_MAX_USERS_DEFAULT = -1//群组最大人数默认值
        val GROUP_AFFILIATIONS_COUNT_DEFAULT = 1//群组人数默认值
        val PERMISSION_NORMAL = 0//普通用户群组权限
        val PERMISSION_OWNER = 1//群组所有者群组权限
        val AVATAR_TYPE_USER = 0//用户头像
        val AVATAR_TYPE_GROUP = 1//群组头像
        val GROUP_PUBLIC = 1//公开群组
        val GROUP_NO_PUBLIC = 0//非公开群组
        val BACKSLASH = "/"//反斜杠
        val AVATAR_TYPE_USER_PATH = "user_avatar"//用户头像保存目录
        val AVATAR_TYPE_GROUP_PATH = "group_icon"//群组头像保存目录
        val AVATAR_SUFFIX_PNG = ".png"//PNG图片后缀名
        val AVATAR_SUFFIX_JPG = ".jpg"//JPG图片后缀名
        val QUESTION = "?"//问号
        val EQUAL = "=" //等号
        val AND = "&" //&符号
        val MSG_PREFIX_MSG = "msg_" //消息码前缀
        val LOCATION_IS_SEARCH_ALLOW = 1//可以被搜索到地理位置
        val LOCATION_IS_SEARCH_INHIBIT = 0//禁止被搜索到地理位置
        val MSG_CONNECTION_SUCCESS = 900//连接服务器成功
        val MSG_CONNECTION_FAIL = 901//连接服务器失败
        val MSG_UPLOAD_AVATAR_SUCCESS = 902//上传头像成功
        val MSG_UPLOAD_AVATAR_FAIL = 903//上传头像失败
        val MSG_REGISTER_SUCCESS = 101//注册成功
        val MSG_REGISTER_USERNAME_EXISTS = 102//账号已经存在
        val MSG_REGISTER_UPLOAD_AVATAR_FAIL = 103//上传头像失败
        val MSG_REGISTER_UPLOAD_AVATAR_SUCCESS = 104//上传头像成功
        val MSG_REGISTER_FAIL = 105//注册失败
        val MSG_UNREGISTER_SUCCESS = 106//注册成功
        val MSG_UNREGISTER_FAIL = 107//注册失败
        val MSG_CONTACT_FIRENDED = 201//已经是好友关系
        val MSG_CONTACT_FAIL = 202//好友关系
        val MSG_GROUP_CREATE_SCUUESS = 301//创建群组成功
        val MSG_GROUP_HXID_EXISTS = 302//群组环信ID已经存在
        val MSG_GROUP_CREATE_FAIL = 303//创建群组成功
        val MSG_GROUP_ADD_MEMBER_FAIL = 304//添加群组成员失败
        val MSG_GROUP_ADD_MEMBER_SCUUESS = 305//添加群组成员成功
        val MSG_GROUP_UNKONW = 306//群组不存在
        val MSG_GROUP_SAME_NAME = 307//群组名称未修改
        val MSG_GROUP_UPDATE_NAME_SUCCESS = 308//群组名称修改成功
        val MSG_GROUP_UPDATE_NAME_FAIL = 309//群组名称修改失败
        val MSG_GROUP_DELETE_MEMBER_SUCCESS = 310//删除群组成员成功
        val MSG_GROUP_DELETE_MEMBER_FAIL = 311//删除群组成员失败
        val MSG_GROUP_DELETE_SUCCESS = 312//删除群组成功
        val MSG_GROUP_DELETE_FAIL = 313//删除群组失败
        val MSG_LOGIN_UNKNOW_USER = 401//账户不存在
        val MSG_LOGIN_ERROR_PASSWORD = 402//账户密码错误
        val MSG_LOGIN_SUCCESS = 403//登陆成功
        val MSG_USER_SAME_NICK = 404//昵称未修改
        val MSG_USER_UPDATE_NICK_SUCCESS = 405//昵称修改成功
        val MSG_USER_UPDATE_NICK_FAIL = 406//昵称修改失败
        val MSG_USER_SAME_PASSWORD = 407//昵称未修改
        val MSG_USER_UPDATE_PASSWORD_SUCCESS = 408//昵称修改成功
        val MSG_USER_UPDATE_PASSWORD_FAIL = 409//昵称修改失败
        val MSG_LOCATION_UPLOAD_SUCCESS = 501//用户上传地理位置成功
        val MSG_LOCATION_UPLOAD_FAIL = 502//用户上传地理位置失败
        val MSG_LOCATION_UPDATE_SUCCESS = 503//用户更新地理位置成功
        val MSG_LOCATION_UPDATE_FAIL = 504//用户更新地理位置失败
        val MSG_UNKNOW = 999//未知错误
        val MSG_ILLEGAL_REQUEST = -1    //非法请求

        /** 上传头像图片的类型：user_avatar或group_icon  */
        val AVATAR_TYPE = "avatarType"
        val AVATAR_SUFFIX = "m_avatar_suffix"
        /** 用户的账号或群组的环信id  */
        val NAME_OR_HXID = "name_or_hxid"
        /** 客户端发送的获取服务端状态的请求  */
        val REQUEST_SERVERSTATUS = "getServerStatus"
        /** 客户端发送的新用户注册的请求  */
        val REQUEST_REGISTER = "register"
        /** 下载图片通用的请求字段  */
        val IMAGE_URL = "imageurl"
        /** 客户端发送的取消注册的请求  */
        val REQUEST_UNREGISTER = "unregister"
        /** 客户端发送的用户登录请求  */
        val REQUEST_LOGIN = "login"
        /** 客户端发送的下载用户头像请求  */
        val REQUEST_DOWNLOAD_AVATAR = "downloadAvatar"
        /** 客户端发送的上传/更新用户头像的请求  */
        val REQUEST_UPDATE_AVATAR = "updateAvatar"
        /** 客户端发送的更新用户昵称的请求  */
        val REQUEST_UPDATE_USER_NICK = "updateNick"
        /** 客户端发送的更新用户密码的请求  */
        val REQUEST_UPDATE_USER_PASSWORD = "updatePassword"
        /** 客户端发送的下载用户的好友列表的全部数据的请求  */
        val REQUEST_DOWNLOAD_CONTACT_ALL_LIST = "downloadContactAllList"
        /** 客户端发送的分页下载用户的好友列表的全部数据的请求  */
        val REQUEST_DOWNLOAD_CONTACT_PAGE_LIST = "downloadContactPageList"
        /** 客户端发送的添加好友的请求  */
        val REQUEST_ADD_CONTACT = "addContact"
        /** 客户端发送的删除好友的请求  */
        val REQUEST_DELETE_CONTACT = "deleteContact"
        /** 客户端发送的根据用户名查找用户信息的请求  */
        val REQUEST_FIND_USER = "findUserByUserName"
        /** 客户端发送的根据用户名或昵称模糊分页查找用户数据的请求  */
        val REQUEST_FIND_USERS_FOR_SEARCH = "findUsersForSearch"

        /** 从服务端查询精选首页的数据 */
        val REQUEST_FIND_BOUTIQUES = "findBoutiques"
        /** 从服务端查询新品首页和精品二级页面的一组商品信息 */
        val REQUEST_FIND_NEW_BOUTIQUE_GOODS = "findNewAndBoutiqueGoods"

        /** 从服务端下载分类首页大类数据 */
        val REQUEST_FIND_CATEGORY_GROUP = "findCategoryGroup"

        /** 从服务端下载分类首页小类数据 */
        val REQUEST_FIND_CATEGORY_CHILDREN = "findCategoryChildren"

        /** 从服务端下载指定商品详细信息的数据 */
        val REQUEST_FIND_GOOD_DETAILS = "findGoodDetails"
        /** 添加收藏 */
        val REQUEST_ADD_COLLECT = "addCollect"
        /** 删除收藏 */
        val REQUEST_DELETE_COLLECT = "deleteCollect"
        /** 下载收藏的商品信息 */
        val REQUEST_FIND_COLLECTS = "findCollects"
        /** 下载收藏的商品数量信息 */
        val REQUEST_FIND_COLLECT_COUNT = "findCollectCount"
        /** 添加商品至购物车 */
        val REQUEST_ADD_CART = "addCart"
        /** 查询用户的购物车 */
        val REQUEST_FIND_CARTS = "findCarts"
        /** 删除购物车中的商品 */
        val REQUEST_DELETE_CART = "deleteCart"
        /** 修改购物车中的商品的信息 */
        val REQUEST_UPDATE_CART = "updateCart"
        /**下载新品首页商品图片 */
        val REQUEST_DOWNLOAD_NEW_GOOD = "downloadNewGood"
        /** 下载商品相册图像的请求 */
        val REQUEST_DOWNLOAD_ALBUM_IMG = "downloadAlbumImg"
        /** 查询是否已收藏 */
        val REQUEST_IS_COLLECT = "isCollect"
        /** 下载精选首页图像的请求 */
        val REQUEST_DOWNLOAD_BOUTIQUE_IMG = "downloadBoutiqueImg"
        /** 下载分类商品大类图像的请求 */
        val REQUEST_DOWNLOAD_CATEGORY_GROUP_IMAGE = "downloadCategoryGroupImage"
        /** 下载分类商品小类图像的请求 */
        val REQUEST_DOWNLOAD_CATEGORY_CHILD_IMAGE = "downloadCategoryChildImage"

        /** 从服务端下载分类二级页面一组商品详情的数据 */
        val REQUEST_FIND_GOODS_DETAILS = "findGoodsDetails"
        /** 下载商品图像的请求 */
        val REQUEST_DOWNLOAD_GOODS_THUMB = "downloadGoodsThumb"
        /** 查询支付情况请求 */
        val REQUEST_FIND_CHARGE = "findCharge"
        /** 支付请求 */
        val REQUEST_PAY = "pay"
        /** 下载图片的请求 */
        val REQUEST_DOWNLOAD_IMAGE = "downloadImage"

        /** 下载精选首页图像的接口 */
        val DOWNLOAD_IMG_URL = SERVER_ROOT +
                REQUEST_DOWNLOAD_IMAGE + QUESTION + IMAGE_URL + "="

        val DOWNLOAD_AVATAR_URL = SERVER_ROOT +
                REQUEST_DOWNLOAD_AVATAR + QUESTION
    }
}
