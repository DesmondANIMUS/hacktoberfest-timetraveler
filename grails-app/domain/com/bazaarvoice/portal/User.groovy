package com.bazaarvoice.portal

import portal.auth.PortalUser

class User implements PortalUser {

	transient springSecurityService

	String username
	String password
    String fullName
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    @Override
    String getDisplayName() {
        return fullName == null ? username : fullName
    }

    static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
