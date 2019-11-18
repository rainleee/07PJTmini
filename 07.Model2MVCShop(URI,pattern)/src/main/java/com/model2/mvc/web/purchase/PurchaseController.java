package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method ���� ����
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView( @RequestParam("prodNo") int prodNo,
									HttpSession session,
									Model model) throws Exception {

		System.out.println("/addPurchaseView.do");
		
		
		model.addAttribute("user",session.getAttribute("user"));
//		System.out.println("addPurchase user : " + session.getAttribute("user"));
		//product�� ������ �������� ���ؼ� ����
		Product product = productService.getProduct(prodNo);
		model.addAttribute("product",product);
		System.out.println("addPurchase product : " + product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase,
								@RequestParam("prodNo") int prodNo,
								HttpSession session,
									Model model) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
//		user�� ������ �������� ���ؼ� ����
		model.addAttribute("user",session.getAttribute("user"));
//		System.out.println("addPurchase user : " + session.getAttribute("user"));
		
		//product�� ������ �������� ���ؼ� ����
		Product product = productService.getProduct(prodNo);
		model.addAttribute("product",product);
//		System.out.println("addPurchase product : " + product);
		purchase.setBuyer((User)session.getAttribute("user"));
		purchase.setPurchaseProd(product);
		
		model.addAttribute("purchase",purchase);
		
		purchaseService.addPurchase(purchase);
		
		//��� ������ �ѱ�����ؼ� redirect���� forward�� ����
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("tranNo") int tranNo ,
									Model model,
									HttpSession session) throws Exception {
		
		System.out.println("/getPurchase.do");
		
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		System.out.println("purchase : " + purchase);
		
		// Model �� View ����
		model.addAttribute("purchase", purchase);
		
		session.setAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
		
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView( @RequestParam("tranNo") int tranNo , Model model,
						HttpSession session) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		// Model �� View ����
		model.addAttribute("purchase", purchase);
		session.getAttribute("purchase");
		session.setAttribute("purchase", purchase);
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase(@RequestParam("tranNo") int tranNo,
									@ModelAttribute("purchase") Purchase purchase,
									Model model) throws Exception{
		
		//����  view.jsp�� �ִ� ������Ʈ ������ purchase�� �־���
		purchaseService.updatePurcahse(purchase);
		
		//������ �ִ� ������ select
		Purchase purchaseSelect = purchaseService.getPurchase(tranNo);
		model.addAttribute("purchase",purchaseSelect);
		
		//Business Logic
		System.out.println("/updatePurchase.do");

		
		
		return "forward:/purchase/updatePurchase.jsp";
		
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search ,
								@ModelAttribute("user") User user,
								Model model, HttpSession session) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
//		System.out.println("search�� ��(��) :" + search);
		search.setPageSize(pageSize);
		//�Ʒ� ������ pageUnit�� ����������. ������ �𵨾�Ʈ����Ʈ �߰�
//		System.out.println("search�� ��(��) :" + search);
		
		user = (User) session.getAttribute("user");
		
		
		
		// Business logic ����
		Map<String , Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		System.out.println("map : " + map);
		
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("model  : " + model);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/listPurchaseProduct.do")
	public String listPurchaseProduct( @ModelAttribute("search") Search search ,
								@ModelAttribute("user") User user,
								Model model,HttpSession session) throws Exception{
		
		System.out.println("/listPurchaseProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		System.out.println("dfdfdfdsf : " + search);
		
		//�Ʒ� ������ pageUnit�� ����������. ������ �𵨾�Ʈ����Ʈ �߰�
		
		user = (User) session.getAttribute("user");
		
		
		
		// Business logic ����
		Map<String , Object> map = purchaseService.getListPurchaseProduct(search, user.getUserId());
		System.out.println("map : " + map);
		
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchaseProduct.jsp";
	}
	
	@RequestMapping("/listSale.do")
	public String listSale( @ModelAttribute("search") Search search ,
								Model model) throws Exception{
		
		System.out.println("/listSale.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		
		

		// Business logic ����
		Map<String , Object> map = purchaseService.getSaleList(search);
		System.out.println("map : " + map);
		
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("model : " + model);
		
		return "forward:/purchase/listSale.jsp";
	}
	
	@RequestMapping("/updateTranCode.do")
	public String updateTranCode(@RequestParam("tranCode") String tranCode,
								@RequestParam("tranNo") int tranNo,
								@RequestParam("currentPage") int currentPage,
								@ModelAttribute("purchase") Purchase purchase,
								@ModelAttribute("search") Search search ,
								Model model ) throws Exception{
		
		
		System.out.println("/updateTranCode.do");
		
		
		purchase.setTranNo(tranNo);
		System.out.println("dsdsd : " + currentPage);
		
		String resultForward = "/listSale.do?CurrentPage=" + currentPage;
		
		
		if(purchase.getTranCode().trim().equals("2")) {
			purchase.setTranCode(tranCode);
			search.setCurrentPage(currentPage);
					
			
		}else if(purchase.getTranCode().trim().equals("3")){
			purchase.setTranCode(tranCode);
			search.setCurrentPage(currentPage);
			
			resultForward = "/listPurchaseProduct.do?CurrentPage=" + currentPage;
		}
		
//		System.out.println("purchase tranCode ��(��) : " + purchase);
		
		// Business logic ����
		purchaseService.updateTranCode(purchase);
		
		
		
		// Model �� View ����
		model.addAttribute("purchase", purchase);
		System.out.println("update purchase  : " + purchase);
		
		return resultForward;
	}
	
//	@RequestMapping("/updateTranCodeByProd.do")
//	public String updateTranCodeByProd(@RequestParam("tranCode") String tranCode,
//								@RequestParam("currentPage") int currentPage,
//								@ModelAttribute("purchase") Purchase purchase,
//								@ModelAttribute("search") Search search ,
//								Model model ) throws Exception{
//		
//		System.out.println("tranCode : " + tranCode);
//		
//		String resultForward = "forward:/purchase/listSale.jsp";
//		
//		System.out.println("/updateTranCodeByProd.do");
//		
//		
//		// Business logic ����
//		
//		System.out.println("purchase tranCode �� : " + purchase);
//		
//		search.setCurrentPage(currentPage);
//		purchase.setTranCode(tranCode);
//		
//		purchaseService.updateTranCode(purchase);
//		
//		// Business logic ����
//		Map<String , Object> map = purchaseService.getSaleList(search);
//		System.out.println("map : " + map);
//				
//		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
//		System.out.println(resultPage);
//		
//		// Model �� View ����
//		model.addAttribute("purchase", purchase);
//		model.addAttribute("resultPage", resultPage);
//		
//		return resultForward;
//	}
	
	
	
}