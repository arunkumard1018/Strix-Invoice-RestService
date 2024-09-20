--- Migration file to retain customers when associated business is
--- Deleted by setting null to business_id

-- Drop the existing foreign key constraint
ALTER TABLE customers
DROP FOREIGN KEY FK_l3n6rbx2m83osa2vbebfgpttq;

-- Add the new foreign key constraint with ON DELETE SET NULL
ALTER TABLE customers
ADD CONSTRAINT FK_l3n6rbx2m83osa2vbebfgpttq
FOREIGN KEY (business_id)
REFERENCES business(id)
ON DELETE SET NULL;