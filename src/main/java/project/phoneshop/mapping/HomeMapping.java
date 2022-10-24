package project.phoneshop.mapping;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class HomeMapping {
    public void getListQuickLink(List<QuickLink> list){
        list.add(new QuickLink(1,"https://salt.tikicdn.com/ts/upload/7b/fc/54/777d24de8eff003bda7a8d5f4294f9a8.gif","Mua sắm có lời",""));
        list.add(new QuickLink(2,"https://salt.tikicdn.com/cache/w100/ts/upload/9c/ca/37/d6e873b1421da32b76654bb274e46683.png.webp","Siêu sale 7.7",""));
        list.add(new QuickLink(3,"https://salt.tikicdn.com/cache/w100/ts/upload/68/9c/2f/6fc82a9a9713a2c2b1968e9760879f6e.png.webp","Đi chợ siêu tốc",""));
        list.add(new QuickLink(4,"https://salt.tikicdn.com/cache/w100/ts/upload/73/e0/7d/af993bdbf150763f3352ffa79e6a7117.png.webp","Dóng tiền, nạp thẻ",""));
        list.add(new QuickLink(5,"https://salt.tikicdn.com/cache/w100/ts/upload/ff/20/4a/0a7c547424f2d976b6012179ed745819.png.webp", "Mua bán ASA/XU", ""));
        list.add(new QuickLink(6,"https://salt.tikicdn.com/cache/w100/ts/upload/73/50/e1/83afc85db37c472de60ebef6eceb41a7.png.webp","Mã giảm giá",""));
        list.add(new QuickLink(7,"https://salt.tikicdn.com/cache/w100/ts/upload/ef/ae/82/f40611ad6dfc68a0d26451582a65102f.png.webp","Bảo hiểm 360",""));
        list.add(new QuickLink(8,"https://salt.tikicdn.com/cache/w100/ts/upload/99/29/ff/cea178635fd5a24ad01617cae66c065c.png.webp","Giảm đến 50%",""));
        list.add(new QuickLink(9,"https://salt.tikicdn.com/cache/w100/ts/upload/99/29/ff/cea178635fd5a24ad01617cae66c065c.png.webp","Hoàn tiền 15%",""));
        list.add(new QuickLink(10,"https://salt.tikicdn.com/cache/w100/ts/upload/4a/b2/c5/b388ee0e511889c83fab1217608fe82f.png.webp","Ưu đãi thanh toán",""));
    }
    public void getListEvent(List<QuickLink> list){
        list.add(new QuickLink(1,"https://cdn.hoanghamobile.com/i/home/Uploads/2022/10/21/iphone-14-1200x382.jpg","",""));
        list.add(new QuickLink(2,"https://cdn.hoanghamobile.com/i/home/Uploads/2022/10/04/tai-nghe-bwoo-1200x382.jpg","",""));
        list.add(new QuickLink(3,"https://cdn.hoanghamobile.com/i/home/Uploads/2022/10/06/laptop-gaming-gigabyte-1200x382.jpg","",""));
        list.add(new QuickLink(4,"https://cdn.hoanghamobile.com/i/home/Uploads/2022/10/19/web-hotsale-samsung-galaxy-a-series-01.jpg","",""));
        list.add(new QuickLink(5,"https://cdn.hoanghamobile.com/i/home/Uploads/2022/10/13/xiaomi-12t-series-1200x382.jpg", "", ""));
        list.add(new QuickLink(6,"https://cdn.hoanghamobile.com/i/home/Uploads/2022/10/11/vivo-v25-series-1200x382.jpg", "", ""));
    }
    public void getListCategorySpecify(List<QuickLink> list){
        list.add(new QuickLink(1,
                "https://salt.tikicdn.com/cache/w100/ts/category/1e/8c/08/d8b02f8a0d958c74539316e8cd437cbd.png.webp",
                "NGON",
                "/ngon"));
        list.add(new QuickLink( 2,
                "https://salt.tikicdn.com/cache/w100/ts/product/0c/b8/11/6c14b804e2649e1f7162f4aef27d1648.jpg.webp",
                "Giày thể thao",
                ""));
        list.add(new QuickLink(3,
                "https://salt.tikicdn.com/cache/w100/ts/product/ad/50/99/93c55f64df94b3809e13e0648eec55f2.jpg.webp",
                "Balo",
                ""));
        list.add(new QuickLink( 4,
                "https://salt.tikicdn.com/cache/w100/ts/product/35/6c/4b/709aef22ee52628dcdbdc857ba1bc46c.jpg.webp",
                "Điện thoại Smartphone",
                ""));
        list.add(new QuickLink( 5,
                "https://salt.tikicdn.com/cache/w100/ts/product/15/d5/1d/64a37269a97a049337a0de82152fd43c.jpg.webp",
                "Nước giặt",
                ""));

    }

    @Data
    @Getter
    @Setter
    public class QuickLink{
        private int id;
        private String alt;
        private String link;
        private String image;

        public QuickLink(int id, String image, String alt, String link) {
            this.id = id;
            this.alt = alt;
            this.link = link;
            this.image = image;
        }
    }
}
