package com.aneagu.birthdaytracker.data.repository.models;

public class Mapper {

    public static BirthdayDto convertFromDaoToDto(Birthday birthday, String authenticatedUserEmail) {
        BirthdayDto birthdayDto = new BirthdayDto();
        birthdayDto.setPhNumber(birthday.getPhNumber());
        birthdayDto.setDeletedOn(birthday.getDeletedOn());
        birthdayDto.setUpdatedOn(birthday.getUpdatedOn());
        birthdayDto.setDate(birthday.getDate());
        birthdayDto.setFullName(birthday.getFullName());
        birthdayDto.setAuthenticatedUserEmail(authenticatedUserEmail);

        return birthdayDto;
    }

    public static Birthday convertFromDtoToDao(BirthdayDto birthdayDto) {
        Birthday birthDay = new Birthday();
        birthDay.setPhNumber(birthdayDto.getPhNumber());
        birthDay.setDeletedOn(birthdayDto.getDeletedOn());
        birthDay.setUpdatedOn(birthdayDto.getUpdatedOn());
        birthDay.setDate(birthdayDto.getDate());
        birthDay.setFullName(birthdayDto.getFullName());
        birthDay.setAuthenticatedEmail(birthdayDto.getAuthenticatedUserEmail());

        return birthDay;
    }
}
