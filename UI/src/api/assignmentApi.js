// assignmentApi.js - Lahat ng HTTP calls para sa Asset Assignments

import axios from "axios";

const BASE_URL = "http://localhost:8080/api/assignments";

// Kunin lahat ng assignments (buong history)
export const getAllAssignments = () => axios.get(BASE_URL);

// Kunin lahat ng ACTIVE assignments (may hawak pa ng asset)
export const getActiveAssignments = () => axios.get(`${BASE_URL}/active`);

// Kunin ang isang assignment gamit ang ID
export const getAssignmentById = (id) => axios.get(`${BASE_URL}/${id}`);

// Kunin lahat ng assignments ng isang user
export const getAssignmentsByUser = (userId) =>
  axios.get(`${BASE_URL}/user/${userId}`);

// Kunin ang history ng isang asset
export const getAssignmentsByAsset = (assetId) =>
  axios.get(`${BASE_URL}/asset/${assetId}`);

// Mag-assign ng asset sa isang empleyado
export const assignAsset = (assignmentData) =>
  axios.post(`${BASE_URL}/assign`, assignmentData);

// I-return ang asset (ibalik ng empleyado)
export const returnAsset = (assignmentId) =>
  axios.patch(`${BASE_URL}/${assignmentId}/return`);

// I-mark ang asset na nawala
export const markAsLost = (assignmentId) =>
  axios.patch(`${BASE_URL}/${assignmentId}/lost`);
