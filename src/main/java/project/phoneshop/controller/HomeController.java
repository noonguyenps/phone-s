package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.phoneshop.mapping.HomeMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/quicklink")
    public List<HomeMapping.QuickLink> quickLink(){
        List<HomeMapping.QuickLink> listQuickLink = new ArrayList<>();
        HomeMapping mapping = new HomeMapping();
        mapping.getListQuickLink(listQuickLink);
        return listQuickLink;
    }
    @GetMapping("/event")
    public List<HomeMapping.QuickLink> event(){
        List<HomeMapping.QuickLink> listEvent = new ArrayList<>();
        HomeMapping mapping = new HomeMapping();
        mapping.getListEvent(listEvent);
        return listEvent;
    }
    @GetMapping("/categories")
    public List<Map<String,Object>> category(){
        Map<String,Object> a = new HashMap<>();
        a.put("id","4384");
        a.put("slug","bach-hoa-online");
        a.put("name","Bách Hóa Online");
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(a);
        return list;
    }
    @GetMapping("/categoryspecify")
    public List<HomeMapping.QuickLink> categorySpecify(){
        List<HomeMapping.QuickLink> list = new ArrayList<>();
        HomeMapping mapping = new HomeMapping();
        mapping.getListCategorySpecify(list);
        return list;
    }
}
