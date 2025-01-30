package com.scm.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.entities.providers;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserServices userServices;

    @Autowired
    private ImageService imageService;

    //add contact handler
    @RequestMapping( "/add")   
    public String addContactView(Model model){
        ContactForm contactForm=new ContactForm();
        // contactForm.setName("Mushfique");
        // contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)   
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication,HttpSession session){

        //process the form data

        //Validate form
        if(result.hasErrors()){
             session.setAttribute("message",Message.builder().content("Fill the form in proper formate").type(MessageType.red).build());
            return "user/add_contact";
        }

        String userName=Helper.getEmailOfLoggedInUser(authentication);
        User user= userServices.getUserByEmail(userName);
        
        
        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        
        //upload image code
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
        String fileName=UUID.randomUUID().toString();

        String fileUrl=imageService.uploadImage(contactForm.getContactImage(),fileName);
        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(fileName);

        }
    
        contactService.save(contact);

        // System.out.println(contactForm);


         session.setAttribute("message",Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());
       

        return "redirect:/user/contacts/add";
    }

    //View contacts handler

    @RequestMapping
    public String viewContacts(@RequestParam(value = "page",defaultValue = "0") int page,
     @RequestParam(value = "size",defaultValue = AppConstant.PAZE_SIZE+"") int size,
     @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
     @RequestParam(value = "direction",defaultValue = "asc") String direction, 
      Model model,Authentication authentication){
        //load all contacts
       String userName= Helper.getEmailOfLoggedInUser(authentication);
       User user=userServices.getUserByEmail(userName);
       
      Page<Contact> pageContact= contactService.getByUser(user,page,size,sortBy,direction);
      model.addAttribute("pageContact", pageContact);
      model.addAttribute("pageSize",AppConstant.PAZE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // Search Handler 

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
     @RequestParam(value = "size",defaultValue = AppConstant.PAZE_SIZE+"") int size,
     @RequestParam(value = "page",defaultValue = "0") int page,
     @RequestParam(value = "sortBy",defaultValue = "name")String sortBy,
     @RequestParam(value = "direction",defaultValue = "asc")String direction,
     Model model,Authentication authentication
     ){

        var user=userServices.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact=contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact=contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
            pageContact=contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstant.PAZE_SIZE);

        System.out.println(pageContact);

        return "user/search";
    }

    //Delete Contact
    @RequestMapping("/delete/{id}")
    
    public String deleteContact(@PathVariable("id") String id,HttpSession session){
        contactService.delete(id);
        session.setAttribute("message",Message.builder().content("You have successfully deleted the contact").type(MessageType.green).build());
        return "redirect:/user/contacts";

    }

    //update contact from

    @GetMapping("/view/{contactId}")
    public String updateContactView(@PathVariable("contactId") String contactId,Model model){
      var contact=  contactService.getById(contactId);

      ContactForm contactForm=new ContactForm();
      contactForm.setName(contact.getName());
      contactForm.setEmail(contact.getEmail());
      contactForm.setPhoneNumber(contact.getPhone());
      contactForm.setAddress(contact.getAddress());
      contactForm.setDescription(contact.getDescription());
      contactForm.setWebsiteLink(contact.getWebsiteLink());
      contactForm.setLinkedInLink(contact.getLinkedInLink());
      contactForm.setFavorite(contact.isFavorite());
      contactForm.setPicture(contact.getPicture());
      model.addAttribute("contactForm", contactForm);
      model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    @RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Model model,HttpSession session){

        if(result.hasErrors()){
           return "user/update_contact_view";
        }

        var contact=contactService.getById(contactId);
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setFavorite(contactForm.isFavorite());
        

        // Image Processing
        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
            String fileName=UUID.randomUUID().toString();
        String imageUrl= imageService.uploadImage(contactForm.getContactImage(), fileName);
        contact.setCloudinaryImagePublicId(fileName);
        contact.setPicture(imageUrl);
        contactForm.setPicture(imageUrl);
        }

       var updatedConatct= contactService.update(contact);
       session.setAttribute("message", Message.builder().content("Contact Updated").type(MessageType.green).build());
        return "redirect:/user/contacts/view/"+contactId;
    }
    
}
