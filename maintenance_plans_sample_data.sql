-- ============================================
-- Sample Maintenance Plans Data
-- Execute this script to populate maintenance plans
-- ============================================

-- Check if data already exists
SELECT COUNT(*) as 'Current MaintenancePlan Count' FROM MaintenancePlan;
SELECT COUNT(*) as 'Current MaintenancePlanItem Count' FROM MaintenancePlanItem;

-- ============================================
-- STEP 1: Delete existing data (optional - uncomment if you want fresh start)
-- ============================================
-- DELETE FROM MaintenancePlanItem;
-- DELETE FROM MaintenancePlan;

-- ============================================
-- STEP 2: Insert Maintenance Plans
-- ============================================

-- 5,000 km - Basic Service
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (5000, 3);

-- 10,000 km - Standard Service
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (10000, 6);

-- 20,000 km - Major Service
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (20000, 12);

-- 30,000 km - Complete Service
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (30000, 18);

-- 40,000 km - Extended Service
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (40000, 24);

-- 50,000 km - Full Overhaul
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (50000, 30);

-- 60,000 km - Comprehensive Service
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (60000, 36);

-- 80,000 km - Major Overhaul
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (80000, 48);

-- 100,000 km - Complete Replacement
INSERT INTO MaintenancePlan (intervalKm, intervalMonths) 
VALUES (100000, 60);

-- ============================================
-- STEP 3: Insert Maintenance Plan Items
-- ============================================

-- Items for 5,000 km plan (Basic Service)
INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Engine Oil Change', 'Engine Oil', id FROM MaintenancePlan WHERE intervalKm = 5000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Oil Filter Replacement', 'Oil Filter', id FROM MaintenancePlan WHERE intervalKm = 5000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Tire Pressure Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 5000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Brake Inspection', NULL, id FROM MaintenancePlan WHERE intervalKm = 5000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Fluid Level Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 5000;

-- Items for 10,000 km plan (Standard Service)
INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Engine Oil Change', 'Engine Oil', id FROM MaintenancePlan WHERE intervalKm = 10000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Oil Filter Replacement', 'Oil Filter', id FROM MaintenancePlan WHERE intervalKm = 10000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Air Filter Replacement', 'Air Filter', id FROM MaintenancePlan WHERE intervalKm = 10000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Brake Pad Inspection', NULL, id FROM MaintenancePlan WHERE intervalKm = 10000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Tire Rotation', NULL, id FROM MaintenancePlan WHERE intervalKm = 10000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Battery Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 10000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Coolant Level Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 10000;

-- Items for 20,000 km plan (Major Service)
INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Engine Oil Change', 'Engine Oil', id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Oil Filter Replacement', 'Oil Filter', id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Air Filter Replacement', 'Air Filter', id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Cabin Air Filter Replacement', 'Cabin Air Filter', id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Brake Fluid Replacement', 'Brake Fluid', id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Spark Plug Inspection', NULL, id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Transmission Fluid Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Suspension Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 20000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Wheel Alignment Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 20000;

-- Items for 30,000 km plan (Complete Service)
INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Engine Oil Change', 'Engine Oil', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Oil Filter Replacement', 'Oil Filter', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Air Filter Replacement', 'Air Filter', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Fuel Filter Replacement', 'Fuel Filter', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Spark Plug Replacement', 'Spark Plugs', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Coolant Replacement', 'Coolant', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Brake Pad Replacement', 'Brake Pads', id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Timing Belt Inspection', NULL, id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Power Steering Fluid Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 30000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Differential Oil Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 30000;

-- Items for 50,000 km plan (Full Overhaul)
INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Complete Engine Service', 'Engine Oil & Parts', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'All Filters Replacement', 'Multiple Filters', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Brake System Overhaul', 'Brake Components', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Transmission Service', 'Transmission Fluid', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Timing Belt Replacement', 'Timing Belt', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Water Pump Inspection', NULL, id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Radiator Service', 'Coolant', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Suspension Overhaul', 'Suspension Parts', id FROM MaintenancePlan WHERE intervalKm = 50000;

INSERT INTO MaintenancePlanItem (taskName, partType, maintenance_plan_id)
SELECT 'Complete Electrical System Check', NULL, id FROM MaintenancePlan WHERE intervalKm = 50000;

-- ============================================
-- STEP 4: Verify the data
-- ============================================
SELECT 'Maintenance Plans:' as 'Data Type', COUNT(*) as 'Count' FROM MaintenancePlan
UNION ALL
SELECT 'Maintenance Plan Items:', COUNT(*) FROM MaintenancePlanItem;

-- Show all plans with their item counts
SELECT 
    mp.id,
    mp.intervalKm as 'Kilometer Interval',
    mp.intervalMonths as 'Month Interval',
    COUNT(mpi.id) as 'Number of Items'
FROM MaintenancePlan mp
LEFT JOIN MaintenancePlanItem mpi ON mp.id = mpi.maintenance_plan_id
GROUP BY mp.id, mp.intervalKm, mp.intervalMonths
ORDER BY mp.intervalKm;

-- Show sample items from each plan
SELECT 
    mp.intervalKm as 'Plan (km)',
    mpi.taskName as 'Task Name',
    mpi.partType as 'Part Type'
FROM MaintenancePlan mp
INNER JOIN MaintenancePlanItem mpi ON mp.id = mpi.maintenance_plan_id
ORDER BY mp.intervalKm, mpi.taskName;
