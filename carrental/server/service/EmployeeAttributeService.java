package com.carrental.server.service;

import com.carrental.server.model.EmployeeAttribute;
import com.carrental.server.model.User;
import com.carrental.server.repository.EmployeeAttributeRepository;
import com.carrental.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeAttributeService {

    @Autowired
    private EmployeeAttributeRepository attributeRepository;

    @Autowired
    private UserRepository userRepository;

    public EmployeeAttribute addAttribute(Long employeeId, String attributeName) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!"EMPLOYEE".equals(employee.getRole())) {
            throw new RuntimeException("User is not an employee");
        }

        EmployeeAttribute attribute = new EmployeeAttribute(employee, attributeName);
        return attributeRepository.save(attribute);
    }

    public void deleteAttribute(Long attributeId) {
        attributeRepository.deleteById(attributeId);
    }

    public List<EmployeeAttribute> getAttributesByEmployeeId(Long employeeId) {
        return attributeRepository.findByEmployeeId(employeeId);
    }

    public List<EmployeeAttribute> getAllAttributes() {
        return attributeRepository.findAll();
    }
}



