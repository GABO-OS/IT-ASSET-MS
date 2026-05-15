// assetApi.js - Lahat ng HTTP calls para sa Assets
// Ginagamit ang axios para mas madali mag-call ng REST API

import axios from "axios";

// Base URL ng backend - kung mag-change ng port, dito lang baguhin
const BASE_URL = "http://localhost:8080/api/assets";

// ===================== ASSET API CALLS =====================

// Kunin lahat ng assets
export const getAllAssets = () => axios.get(BASE_URL);

// Kunin ang isang asset gamit ang ID
export const getAssetById = (id) => axios.get(`${BASE_URL}/${id}`);

// Kunin ang assets ayon sa status (AVAILABLE, IN_USE, etc.)
export const getAssetsByStatus = (status) =>
  axios.get(`${BASE_URL}/status/${status}`);

// Kunin ang assets ayon sa type (LAPTOP, MONITOR, etc.)
export const getAssetsByType = (type) =>
  axios.get(`${BASE_URL}/type/${type}`);

// Maghanap ng asset gamit ang name
export const searchAssets = (name) =>
  axios.get(`${BASE_URL}/search`, { params: { name } });

// Mag-add ng bagong asset
export const createAsset = (assetData) =>
  axios.post(BASE_URL, assetData);

// I-update ang impormasyon ng asset
export const updateAsset = (id, assetData) =>
  axios.put(`${BASE_URL}/${id}`, assetData);

// I-update lang ang status ng asset
export const updateAssetStatus = (id, status) =>
  axios.patch(`${BASE_URL}/${id}/status/${status}`);

// I-delete ang asset
export const deleteAsset = (id) => axios.delete(`${BASE_URL}/${id}`);
