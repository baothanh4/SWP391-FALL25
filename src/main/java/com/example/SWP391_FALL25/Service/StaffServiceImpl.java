package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.UpdateUserProfileRequest;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Enum.Role;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import com.example.SWP391_FALL25.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private ServiceAppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SystemLogService log;

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<ServiceAppointment> appointments = appointmentRepository.findAll();
        List<AppointmentDTO> result = new ArrayList<>();

        for (ServiceAppointment a : appointments) {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setAppointmentId(a.getId());
            dto.setCustomerId(a.getVehicle().getCustomer().getId());  // lấy customer qua vehicle
            dto.setVehicleId(a.getVehicle().getId());
            dto.setServiceCenterId(a.getServiceCenter().getId());
            dto.setStatus(a.getStatus().name());
            dto.setAppointmentDate(a.getAppointmentDate());
            dto.setAppointmentTime(a.getAppointmentTime());
            dto.setTechnicianAssigned(a.getTechnicianAssigned());

            result.add(dto);
        }
        return result;
    }

    @Override
    public void updateAppointmentStatus(Long appointmentId){
        ServiceAppointment appointment=appointmentRepository.findById(appointmentId).orElseThrow(()->new IllegalArgumentException("appointment not found"));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        String customerEmail=appointment.getVehicle().getCustomer().getEmail();
        String customerFullName=appointment.getVehicle().getCustomer().getFullname();

        String subject = "Xe của bạn đã sẵn sàng để nhận!";
        String message = String.format(
                "Kính gửi %s,\n\nXe của bạn đã được bảo dưỡng và hiện đã sẵn sàng để nhận.\n" +
                        "Mã lịch hẹn dịch vụ: %d\n" +
                        "Ngày hẹn: %s\n\nCảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!\n\nTrân trọng,\nTrung tâm dịch vụ.",
                customerFullName,
                appointment.getId(),
                appointment.getAppointmentDate()
        );

        try {
            emailService.sendEmail(customerEmail, subject, message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    @Transactional
    public Users updateStaffProfile(Long staffId, UpdateUserProfileRequest request) {
        Users staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staff.getRole() != Role.STAFF) {
            throw new RuntimeException("User is not a staff");
        }

        userService.updateProfile(staff.getId(), request);
        systemLogService.log(staff.getId(), "UPDATE STAFF PROFILE");

        return userRepository.save(staff);
    }

}
