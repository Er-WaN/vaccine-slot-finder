import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './vaccination-slot.reducer';

export interface IVaccinationSlotDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VaccinationSlotDeleteDialog = (props: IVaccinationSlotDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/vaccination-slot');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.vaccinationSlotEntity.id);
  };

  const { vaccinationSlotEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="vaccinationSlotDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="vaccineSlotFinderApp.vaccinationSlot.delete.question">Are you sure you want to delete this VaccinationSlot?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-vaccinationSlot" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ vaccinationSlot }: IRootState) => ({
  vaccinationSlotEntity: vaccinationSlot.entity,
  updateSuccess: vaccinationSlot.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationSlotDeleteDialog);
