// userApi.js - Lahat ng HTTP calls para sa Users/Employees

import axios from "axios";

const BASE_URL = "http://localhost:8080/api/users";

// Kunin lahat ng users
export const getAllUsers = () => axios.get(BASE_URL);

// Kunin lahat ng active users lang
export const getActiveUsers = () => axios.get(`${BASE_URL}/active`);

// Kunin ang isang user gamit ang ID
export const getUserById = (id) => axios.get(`${BASE_URL}/${id}`);

// Maghanap ng user gamit ang pangalan
export const searchUsers = (name) =>
  axios.get(`${BASE_URL}/search`, { params: { name } });

// Kunin lahat ng users sa isang department
export const getUsersByDepartment = (department) =>
  axios.get(`${BASE_URL}/department/${department}`);

// Mag-register ng bagong user/empleyado
export const createUser = (userData) => axios.post(BASE_URL, userData);

// I-update ang impormasyon ng user
export const updateUser = (id, userData) =>
  axios.put(`${BASE_URL}/${id}`, userData);

// I-deactivate ang user (soft delete - hindi ganap na natanggal)
export const deactivateUser = (id) =>
  axios.patch(`${BASE_URL}/${id}/deactivate`);

// Hard delete ng user
export const deleteUser = (id) => axios.delete(`${BASE_URL}/${id}`);
