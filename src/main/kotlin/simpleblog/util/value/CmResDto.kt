package simpleblog.util.value

class CmResDto<T>(
    val resultCode:T,
    val resultMsg:String,
    val data:T
) {
}
